package Hongik_SafeMap_Server.domain.disaster_report_group.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record GroupDetailResponse(
        Long groupId,
        DisasterTypeResponse disasterType,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Double centerLatitude,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Double centerLongitude,
        LocalDateTime earliestReportTime,
        LocalDateTime latestReportTime,
        int reportCount,
        RiskLevel latestRiskLevel,
        boolean isActive,
        String title,
        List<DisasterReportListResponse> reports
) {
    public GroupDetailResponse {
        centerLatitude = centerLatitude != null ? Math.round(centerLatitude * 1000000.0) / 1000000.0 : null;
        centerLongitude = centerLongitude != null ? Math.round(centerLongitude * 1000000.0) / 1000000.0 : null;
    }
}
