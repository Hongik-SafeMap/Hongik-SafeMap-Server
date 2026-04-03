package Hongik_SafeMap_Server.domain.admin.account.service;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.DemoteFromAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.PromoteToAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.UpdateAdminNicknameRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.response.AdminAccountResponse;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.global.annotation.LogAdminActivity;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @LogAdminActivity(description = "사용자를 관리자로 승급했습니다.")
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

    @LogAdminActivity(description = "관리자 권한을 박탈했습니다.")
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

    @LogAdminActivity(description = "관리자 닉네임을 변경했습니다.")
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

    public List<AdminAccountResponse> getAdminAccounts() {
        // 모든 관리자 계정 조회
        List<Member> adminMembers = memberRepository.findAllByStatus(MemberStatus.ADMIN);

        // AdminAccountResponse로 변환
        return adminMembers.stream()
                .map(AdminAccountResponse::of)
                .collect(Collectors.toList());
    }
}
