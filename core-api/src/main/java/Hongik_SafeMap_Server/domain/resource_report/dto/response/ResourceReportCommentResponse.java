package Hongik_SafeMap_Server.domain.resource_report.dto.response;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReportComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "자원 게시글 댓글 응답")
public record ResourceReportCommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "댓글 내용", example = "저희가 도와드릴 수 있어요!")
        String content,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "작성자", example = "홍길동")
        String memberName
) {
    public static ResourceReportCommentResponse of(ResourceReportComment resourceReportComment) {
        return new ResourceReportCommentResponse(
                resourceReportComment.getId(),
                resourceReportComment.getContent(),
                resourceReportComment.getCreatedAt(),
                resourceReportComment.getMember().getId(),
                resourceReportComment.getMember().getName()
        );
    }
}