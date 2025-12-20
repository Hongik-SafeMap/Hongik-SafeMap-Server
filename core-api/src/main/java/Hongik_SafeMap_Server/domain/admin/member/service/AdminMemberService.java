package Hongik_SafeMap_Server.domain.admin.member.service;

import Hongik_SafeMap_Server.domain.admin.member.dto.AdminMemberResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.MEMBER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;
    private final DisasterReportRepository disasterReportRepository;

    @Transactional(readOnly = true)
    public List<AdminMemberResponse> findAllMembers() {
        return disasterReportRepository.findAdminMemberList();
    }

    // 공신력 부여/해제
    @Transactional
    public void toggleCredible(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_EXISTS));

        member.toggleCredible();
    }
}
