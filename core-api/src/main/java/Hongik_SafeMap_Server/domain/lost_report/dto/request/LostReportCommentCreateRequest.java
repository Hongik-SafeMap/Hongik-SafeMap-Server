package Hongik_SafeMap_Server.domain.lost_report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "실종신고 댓글 작성 요청")
public record LostReportCommentCreateRequest(
        @Schema(description = "댓글 내용", example = "제가 어제 그 근처에서 봤어요!")
        @NotBlank(message = "댓글 내용은 필수입니다")
        String content
) {
}