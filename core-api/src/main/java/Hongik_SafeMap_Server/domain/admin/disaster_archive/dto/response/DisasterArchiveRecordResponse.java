package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record DisasterArchiveRecordResponse(
        Long id,
        DisasterTypeResponse disasterType,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Double centerLatitude,
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Double centerLongitude,
        LocalDateTime earliestReportTime,
        LocalDateTime latestReportTime,
        int reportCount,
        int approvedReportCount,
        int pendingReportCount,
        RiskLevel latestRiskLevel,
        @Schema(example = "서울특별시 마포구 와우산로 94")
        String address
) {
    public DisasterArchiveRecordResponse {
        centerLatitude = centerLatitude != null ? Math.round(centerLatitude * 1000000.0) / 1000000.0 : null;
        centerLongitude = centerLongitude != null ? Math.round(centerLongitude * 1000000.0) / 1000000.0 : null;
    }
}
