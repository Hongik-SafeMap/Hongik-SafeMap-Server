package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.time.LocalDateTime;

public record DisasterReportListResponse(
        Long id,
        DisasterType disasterType,
        RiskLevel riskLevel,
        String disasterDescription,
        String address,
        DisasterReportStatus status,
        LocalDateTime createdAt
) {
    public static DisasterReportListResponse of(DisasterReport dr) {
        return new DisasterReportListResponse(
                dr.getId(),
                dr.getDisasterType(),
                dr.getRiskLevel(),
                dr.getDisasterDescription(),
                dr.getAddress(),
                dr.getStatus(),
                dr.getCreatedAt()
        );
    }
}
