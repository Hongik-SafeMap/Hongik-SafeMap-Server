package Hongik_SafeMap_Server.domain.lost_report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record LostReportsResponse(
        @Schema(description = "게시글 개수", example = "1")
        int totalCount,

        @Schema(description = "게시글 목록")
        List<LostReportResponse> reports
) {
}
