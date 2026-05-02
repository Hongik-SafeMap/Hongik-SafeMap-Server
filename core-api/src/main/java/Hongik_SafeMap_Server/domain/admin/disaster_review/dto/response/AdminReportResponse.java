package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record AdminReportResponse(
        @Schema(description = "제보 ID", example = "1")
        Long reportId,

        @Schema(description = "재난 유형")
        DisasterTypeResponse disasterType,

        @Schema(description = "제보 내용", example = "도로에 싱크홀이 발생했습니다")
        String description,

        @Schema(description = "제보 상태")
        DisasterReportStatus status,

        @Schema(description = "검토 의견", example = "신뢰할 수 있는 제보로 판단됩니다.")
        String reviewComment,

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
                DisasterTypeResponse.of(disasterReport.getDisasterType()),
                disasterReport.getDisasterDescription(),
                disasterReport.getStatus(),
                disasterReport.getReviewComment(),
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
                DisasterTypeResponse.of(disasterReport.getDisasterType()),
                disasterReport.getDisasterDescription(),
                disasterReport.getStatus(),
                disasterReport.getReviewComment(),
                helpfulCount,
                notHelpfulCount,
                accusationCount
        );
    }
}
