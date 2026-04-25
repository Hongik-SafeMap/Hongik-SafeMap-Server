package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GroupLocationResponse(
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
        @Schema(example = "서울특별시 마포구 와우산로 94")
        String earliestAddress,
        List<ReportLocation> reportLocations
) {
    public static GroupLocationResponse of(DisasterReportGroup group, List<DisasterReport> reports) {
        List<ReportLocation> locations = reports.stream()
                .map(ReportLocation::of)
                .toList();
        return new GroupLocationResponse(
                group.getId(),
                DisasterTypeResponse.of(group.getDisasterType()),
                group.getCenterLatitude(),
                group.getCenterLongitude(),
                group.getEarliestReportTime(),
                group.getLatestReportTime(),
                group.getReportCount(),
                group.getLatestRiskLevel(),
                group.isActive(),
                group.getEarliestAddress(),
                locations
        );
    }

    public record ReportLocation(
            Long reportId,
            @JsonFormat(shape = JsonFormat.Shape.NUMBER)
            Double latitude,
            @JsonFormat(shape = JsonFormat.Shape.NUMBER)
            Double longitude,
            RiskLevel riskLevel
    ) {
        public static ReportLocation of(DisasterReport report) {
            return new ReportLocation(
                    report.getId(),
                    report.getLatitude(),
                    report.getLongitude(),
                    report.getRiskLevel()
            );
        }
    }
}
