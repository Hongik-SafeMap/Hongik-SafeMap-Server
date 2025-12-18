package Hongik_SafeMap_Server.util;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static Hongik_SafeMap_Server.exception.ErrorMessage.MEMBER_NOT_EXISTS_WITH_EMAIL;

@Component
@RequiredArgsConstructor
public class MemberUtil {
    private final MemberRepository memberRepository;

    public Member getLoggedInMember() {
        return memberRepository.findByEmail(getLoggedInMemberEmail()).orElseThrow(
                () -> new MemberException(MEMBER_NOT_EXISTS_WITH_EMAIL)
        );
    }

    private String getLoggedInMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
