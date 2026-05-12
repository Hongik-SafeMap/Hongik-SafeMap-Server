package Hongik_SafeMap_Server.domain.auth.service;


import Hongik_SafeMap_Server.domain.auth.dto.response.LoginResponse;
import Hongik_SafeMap_Server.domain.auth.domain.RefreshToken;
import Hongik_SafeMap_Server.domain.auth.dto.request.SnsLoginRequest;
import Hongik_SafeMap_Server.domain.auth.dto.response.SnsAuthResponse;
import Hongik_SafeMap_Server.domain.auth.repository.RefreshTokenRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.util.TokenUtil;
import Hongik_SafeMap_Server.vo.LoginType;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final SnsService snsService;

    @Transactional
    public LoginResponse generalLogin(String email, String password, String fcmToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(EMAIL_DOES_NOT_EXIST));

        if (member.getLoginType() != LoginType.GENERAL) {
            throw new MemberException(INVALID_LOGIN_TYPE);
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(PASSWORD_IS_DIFFERENT_FROM_CHECK);
        }

        // FCM 토큰 업데이트
        if (fcmToken != null && !fcmToken.isEmpty()) {
            member.updateFcmToken(fcmToken);
        }

        return issueTokensAndSaveToDB(member);
    }

    @Transactional
    public LoginResponse processSnsLogin(SnsLoginRequest request) {
        LoginType loginType = LoginType.from(request.loginType());

        SnsAuthResponse snsData = snsService.verifyToken(
                request.token(),
                loginType
        );

        String email = snsData.email();
        String socialId = snsData.socialId();
        String name = snsData.name();
        String phone = snsData.phone();

        Member member = memberRepository.findByLoginTypeAndSocialId(loginType, socialId)
                .orElseGet(() -> {
                    memberRepository.findByEmail(email).ifPresent(existingMember -> {
                        throw new MemberException(SOCIAL_ACCOUNT_ALREADY_REGISTERED_WITH_OTHER_TYPE);
                    });

                    return memberRepository.save(Member.builder()
                            .email(email)
                            .password(null)
                            .name(name != null && !name.isBlank() ? name : "사용자")
                            .phone(phone)
                            .status(MemberStatus.USER)
                            .loginType(loginType)
                            .socialId(socialId)
                            .fcmToken(request.fcmToken())
                            .build());
                });

        if (request.fcmToken() != null && !request.fcmToken().isBlank()) {
            member.updateFcmToken(request.fcmToken());
        }

        return issueTokensAndSaveToDB(member);
    }

    // refreshToken 재발급
    @Transactional
    public LoginResponse reissue(String refreshToken) {
        // 토큰 형식/서명/만료 검증
        if (!tokenUtil.validateToken(refreshToken)) {
            throw new MemberException(INVALID_REFRESH_TOKEN);
        }

        // refreshToken에서 email 추출
        String email = tokenUtil.getEmailFromToken(refreshToken);

        // DB에 저장된 refresh와 일치하는지 확인
        RefreshToken stored = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException("로그아웃 되었거나 세션이 만료되었습니다."));

        if (!stored.getRefreshToken().equals(refreshToken)) {
            throw new MemberException(REFRESH_TOKEN_DOES_NOT_MATCH);
        }

        // 회원 정보 기반으로 새 토큰 발급 + DB 갱신
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS_WITH_EMAIL));

        return issueTokensAndSaveToDB(member);
    }

    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }

    private LoginResponse issueTokensAndSaveToDB(Member member) {
        String accessToken = tokenUtil.generateAccessToken(member.getEmail(), member.getName(), member.getStatus());
        String refreshToken = tokenUtil.generateRefreshToken(member.getEmail(), member.getName(), member.getStatus());

        RefreshToken tokenEntity = refreshTokenRepository.findByEmail(member.getEmail())
                .orElse(new RefreshToken(member.getEmail(), refreshToken));

        tokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(tokenEntity);

        return LoginResponse.of(member, accessToken, refreshToken);
    }
}
