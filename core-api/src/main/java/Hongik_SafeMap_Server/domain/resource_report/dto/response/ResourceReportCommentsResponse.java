package Hongik_SafeMap_Server.domain.resource_report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "자원 게시글 댓글 목록 응답")
public record ResourceReportCommentsResponse(
        @Schema(description = "댓글 개수", example = "1")
        int totalCount,

        @Schema(description = "댓글 목록")
        List<ResourceReportCommentResponse> comments
) {
}