package Hongik_SafeMap_Server.domain.lost_report.dto.response;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReportComment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "실종 신고 댓글 응답")
public record LostReportCommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "댓글 내용", example = "제가 어제 그 근처에서 봤어요!")
        String content,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "작성자", example = "홍길동")
        String memberName
) {
    public static LostReportCommentResponse of(LostReportComment lostReportComment) {
        return new LostReportCommentResponse(
                lostReportComment.getId(),
                lostReportComment.getContent(),
                lostReportComment.getCreatedAt(),
                lostReportComment.getMember().getId(),
                lostReportComment.getMember().getName()
        );
    }
}
