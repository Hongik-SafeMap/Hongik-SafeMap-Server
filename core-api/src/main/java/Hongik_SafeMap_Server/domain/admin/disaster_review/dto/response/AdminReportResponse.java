package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterType;
import io.swagger.v3.oas.annotations.media.Schema;

public record AdminReportResponse(
        @Schema(description = "제보 ID", example = "1")
        Long reportId,

        @Schema(description = "재난 유형")
        DisasterType disasterType,

        @Schema(description = "제보 내용", example = "도로에 싱크홀이 발생했습니다")
        String description,

        @Schema(description = "도움됨 수", example = "25")
        int helpfulCount,

        @Schema(description = "도움 안됨 수", example = "3")
        int notHelpfulCount,

        @Schema(description = "신고 수", example = "2")
        int accusationCount
) {
    public static AdminReportResponse of(DisasterReport disasterReport) {
        return new AdminReportResponse(
                disasterReport.getId(),
                disasterReport.getDisasterType(),
                disasterReport.getDisasterDescription(),
                0,
                0,
                0
        );
    }

    public static AdminReportResponse of(
            DisasterReport disasterReport,
            int helpfulCount,
            int notHelpfulCount,
            int accusationCount
    ) {
        return new AdminReportResponse(
                disasterReport.getId(),
                disasterReport.getDisasterType(),
                disasterReport.getDisasterDescription(),
                helpfulCount,
                notHelpfulCount,
                accusationCount
        );
    }
}
