package Hongik_SafeMap_Server.domain.admin.dashboard.service;

import Hongik_SafeMap_Server.domain.admin.dashboard.dto.AdminDashboardResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final DisasterReportRepository disasterReportRepository;
    private final MemberRepository memberRepository;

    public AdminDashboardResponse getDashboard() {
        long totalReports = disasterReportRepository.count();
        long pendingReports = 0L;  // @TODO: 검토 대기
        long totalUsers = memberRepository.count();
        long blindedReports = disasterReportRepository.countByStatus(DisasterReportStatus.BLINDED);
        long suspiciousUsers = 0L; // @TODO: 신뢰도 의심
        long credibleUsers = memberRepository.countByIsCredibleTrueAndStatus(MemberStatus.USER);

        List<AdminDashboardResponse.RecentReport> recentReports = disasterReportRepository.findTop4ByOrderByCreatedAtDesc()
                .stream()
                .map(AdminDashboardResponse.RecentReport::from)
                .toList();

        return AdminDashboardResponse.of(
                totalReports,
                pendingReports,
                totalUsers,
                blindedReports,
                suspiciousUsers,
                credibleUsers,
                recentReports
        );
    }
}
