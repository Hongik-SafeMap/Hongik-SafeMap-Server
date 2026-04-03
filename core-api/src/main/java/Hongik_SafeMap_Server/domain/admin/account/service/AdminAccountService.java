package Hongik_SafeMap_Server.domain.admin.account.service;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.PromoteToAdminRequest;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.MEMBER_NOT_EXISTS_WITH_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAccountService {

    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;

    public AdminMyPageResponse getAdminMyPage() {
        Member member = memberUtil.getLoggedInMember();
        return AdminMyPageResponse.of(member);
    }

    @Transactional
    public void promoteToAdmin(PromoteToAdminRequest request) {
        // 이메일로 기존 회원 찾기
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS_WITH_EMAIL));

        // 관리자용 닉네임 업데이트
        member.updateNickname(request.getNickname());

        // 관리자로 승급
        member.promoteToAdmin();
    }
}
