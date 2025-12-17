package Hongik_SafeMap_Server.domain.auth.service;

import Hongik_SafeMap_Server.domain.auth.dto.LoginResponse;
import Hongik_SafeMap_Server.domain.auth.domain.RefreshToken;
import Hongik_SafeMap_Server.domain.auth.repository.RefreshTokenRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.execption.MemberException;
import Hongik_SafeMap_Server.util.TokenUtil;
import Hongik_SafeMap_Server.vo.LoginType;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.execption.ErrorMessage.EMAIL_DOES_NOT_EXIST;
import static Hongik_SafeMap_Server.execption.ErrorMessage.PASSWORD_IS_DIFFERENT_FROM_CHECK;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    // =================================================================================
    // 1. 일반 로그인 (이메일 + 비밀번호)
    // =================================================================================
    @Transactional
    public LoginResponse generalLogin(String email, String password) {
        // 1. 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(EMAIL_DOES_NOT_EXIST));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(PASSWORD_IS_DIFFERENT_FROM_CHECK);
        }

        // 3. 토큰 발급, DB 저장 및 응답 객체 반환 (코드가 훨씬 짧아짐)
        return issueTokensAndSaveToDB(member);
    }

    // =================================================================================
    // 2. SNS 로그인 (토큰 검증 + 자동 회원가입 + 로그인)
    // =================================================================================
    @Transactional
    public LoginResponse snsLogin(String email, String socialId, LoginType loginType) {

        // 1. DB 조회 후 없으면 자동 회원가입(save), 있으면 조회(get)
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("신규 SNS 유저 회원가입 진행: {}", email);
                    return memberRepository.save(Member.builder()
                            .email(email)
                            .password(null)
                            .name("임시닉네임")
                            .phone("")
                            .status(MemberStatus.일반)
                            .loginType(loginType)
                            .socialId(socialId)
                            .build());
                });

        // 2. 토큰 발급, DB 저장 및 응답 객체 반환
        return issueTokensAndSaveToDB(member);
    }

    // =================================================================================
    // 3. 로그아웃
    // =================================================================================
    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteByEmail(email);
        log.info("로그아웃 완료 (Refresh Token 삭제): {}", email);
    }

    // =================================================================================
    // [공통 메서드] 토큰 생성 -> DB 저장 -> LoginResponse 반환
    // =================================================================================
    private LoginResponse issueTokensAndSaveToDB(Member member) {
        // 1. 토큰 생성 (Access, Refresh)
        String accessToken = tokenUtil.generateAccessToken(member.getEmail(), member.getName(), member.getStatus());
        String refreshToken = tokenUtil.generateRefreshToken(member.getEmail(), member.getName(), member.getStatus());

        // 2. DB에 Refresh Token 저장 (기존에 있으면 업데이트, 없으면 생성)
        RefreshToken tokenEntity = refreshTokenRepository.findByEmail(member.getEmail())
                .orElse(new RefreshToken(member.getEmail(), refreshToken));

        tokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(tokenEntity);

        // 3. LoginResponse에 accessToken과 refreshToken을 모두 담아서 리턴
        // LoginResponse.of 팩토리 메서드 인자가 (Member, AccessToken, RefreshToken) 순서라고 가정
        return LoginResponse.of(member, accessToken, refreshToken);
    }
}
