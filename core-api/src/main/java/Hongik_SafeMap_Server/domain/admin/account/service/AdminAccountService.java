package Hongik_SafeMap_Server.domain.admin.account.service;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.DemoteFromAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.PromoteToAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.UpdateAdminNicknameRequest;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

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

    @Transactional
    public void demoteFromAdmin(DemoteFromAdminRequest request) {
        // 이메일로 기존 회원 찾기
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS_WITH_EMAIL));

        // 관리자 권한 확인
        if (!MemberStatus.ADMIN.equals(member.getStatus())) {
            throw new MemberException(MEMBER_IS_NOT_ADMIN);
        }

        // 관리자 권한 박탈
        member.demoteFromAdmin();
    }

    @Transactional
    public void updateAdminNickname(UpdateAdminNicknameRequest request) {
        // 회원 ID로 기존 회원 찾기
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));

        // 관리자 권한 확인
        if (!MemberStatus.ADMIN.equals(member.getStatus())) {
            throw new MemberException(MEMBER_IS_NOT_ADMIN);
        }

        // 관리자 닉네임 업데이트
        member.updateNickname(request.getNickname());
    }
}
