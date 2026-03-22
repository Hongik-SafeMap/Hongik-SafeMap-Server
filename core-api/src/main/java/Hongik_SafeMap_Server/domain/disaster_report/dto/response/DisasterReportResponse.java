package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;

public record DisasterReportResponse(
        Long id,
        DisasterType disasterType,
        RiskLevel riskLevel,
        String disasterDescription,
        Double latitude,
        Double longitude,
        String address,
        List<String> fileUrls,
        DisasterReportStatus status,
        LocalDateTime createdAt,
        Long memberId
) {
    public static DisasterReportResponse of(DisasterReport dr){
        return new DisasterReportResponse(
                dr.getId(),
                dr.getDisasterType(),
                dr.getRiskLevel(),
                dr.getDisasterDescription(),
                dr.getLatitude(),
                dr.getLongitude(),
                dr.getAddress(),
                dr.getFileUrls(),
                dr.getStatus(),
                dr.getCreatedAt(),
                dr.getMember().getId()
        );
    }
}
