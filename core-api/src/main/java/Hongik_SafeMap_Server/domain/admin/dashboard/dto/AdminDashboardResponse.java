package Hongik_SafeMap_Server.domain.admin.dashboard.dto;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;

public record AdminDashboardResponse(
        long totalReports,
        long falseReports,
        long totalUsers,
        long suspiciousUsers,
        long blindedReports,
        long credibleUsers,
        List<RecentReport> recentReports
) {
    public static AdminDashboardResponse of(
            long totalReports,
            long pendingReports,
            long totalUsers,
            long blindedReports,
            long suspiciousReports,
            long credibleUsers,
            List<RecentReport> recentReports
    ) {
        return new AdminDashboardResponse(
                totalReports, // 총 제보 수
                pendingReports, // 검토 대기
                totalUsers, // 총 사용자
                blindedReports, // 블라인드 처리
                suspiciousReports, // 신뢰도 의심
                credibleUsers, // 공신력 사용자
                recentReports // 최근 등록된 제보
        );
    }

    public record RecentReport(
            Long id,
            DisasterType disasterType,
            RiskLevel riskLevel,
            String disasterDescription,
            DisasterReportStatus status,
            LocalDateTime createdAt
    ) {
        public static RecentReport from(DisasterReport report) {
            return new RecentReport(
                    report.getId(),
                    report.getDisasterType(),
                    report.getRiskLevel(),
                    report.getDisasterDescription(),
                    report.getStatus(),
                    report.getCreatedAt()
            );
        }
    }
}
