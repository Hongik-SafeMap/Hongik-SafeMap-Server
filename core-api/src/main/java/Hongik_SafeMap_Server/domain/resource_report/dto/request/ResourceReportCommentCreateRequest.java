package Hongik_SafeMap_Server.domain.resource_report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "자원 게시글 댓글 작성 요청")
public record ResourceReportCommentCreateRequest(
        @Schema(description = "댓글 내용", example = "저희가 도와드릴 수 있어요!")
        @NotBlank(message = "댓글 내용은 필수입니다")
        String content
) {
}