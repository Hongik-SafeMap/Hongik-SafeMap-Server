package Hongik_SafeMap_Server.domain.disaster_report_group.dto.response;

import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;

public record GroupedDisasterReportResponse(
        DisasterType disasterType,
        Double centerLatitude,
        Double centerLongitude,
        LocalDateTime earliestReportTime,
        LocalDateTime latestReportTime,
        int reportCount,
        RiskLevel latestRiskLevel,
        List<Long> reportIds
) {
}