package Hongik_SafeMap_Server.domain.admin.disaster_review;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterType;

public record AdminReportListResponse(
        Long reportId,
        DisasterType disasterType,
        String description
) {
    public static AdminReportListResponse of(DisasterReport disasterReport) {
        return new AdminReportListResponse(
                disasterReport.getId(),
                disasterReport.getDisasterType(),
                disasterReport.getDisasterDescription()
        );
    }
}
