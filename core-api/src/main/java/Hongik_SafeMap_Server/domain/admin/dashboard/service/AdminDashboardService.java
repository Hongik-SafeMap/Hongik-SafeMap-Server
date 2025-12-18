package Hongik_SafeMap_Server.domain.admin.dashboard.service;

import Hongik_SafeMap_Server.domain.admin.dashboard.dto.AdminDashboardResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final DisasterReportRepository disasterReportRepository;
    private final MemberRepository memberRepository;

    public AdminDashboardResponse getDashboard() {
        long totalReports = disasterReportRepository.count();
        long totalUsers = memberRepository.count();
        long credibleUsers = memberRepository.countByIsCredibleTrueAndStatus(MemberStatus.USER);

        return AdminDashboardResponse.of(totalReports, totalUsers, credibleUsers);
    }
}
