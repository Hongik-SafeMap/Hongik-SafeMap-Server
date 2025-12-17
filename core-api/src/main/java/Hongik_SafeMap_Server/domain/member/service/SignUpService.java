package Hongik_SafeMap_Server.domain.member.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.LoginType;
import Hongik_SafeMap_Server.vo.MemberStatus;
import Hongik_SafeMap_Server.domain.member.dto.SignUpRequest;
import Hongik_SafeMap_Server.domain.member.dto.SignUpResponse;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.execption.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.execption.ErrorMessage.DUPLICATED_EMAIL;
import static Hongik_SafeMap_Server.execption.ErrorMessage.PASSWORD_IS_DIFFERENT_FROM_CHECK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignUpService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse registerMember(SignUpRequest signUpRequest) {
        //이메일 중복체크
        final boolean isDuplicatedEmail = memberRepository.existsByEmail(signUpRequest.email());
        if (isDuplicatedEmail) {
            throw new MemberException(DUPLICATED_EMAIL);
        }

        //비밀번호, 비밀번호 확인 일치하는지 확인
        if(!signUpRequest.password().equals(signUpRequest.passwordConfirm())) {
            throw new MemberException(PASSWORD_IS_DIFFERENT_FROM_CHECK);
        }

        //비밀번호 암호화
        String encodePassword = passwordEncoder.encode(signUpRequest.password());

        //암호화된 비밀번호로 새로운 member 생성
        Member member = Member.builder()
                .email(signUpRequest.email())
                .password(encodePassword)
                .name(signUpRequest.name())
                .phone(signUpRequest.phone())
                .loginType(LoginType.일반)
                .socialId(null)
                .build();

        //Member 저장
        Member savedMember = memberRepository.save(member);

        return new SignUpResponse(savedMember.getName(), savedMember.getEmail(), savedMember.getPhone());
    }

}
