package Hongik_SafeMap_Server.domain.lost_report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "실종 신고 게시물 댓글 목록 응답")
public record LostReportCommentsResponse(
        @Schema(description = "댓글 개수", example = "2")
        int totalCount,

        @Schema(description = "댓글 목록")
        List<LostReportCommentResponse> comments
) {
}
