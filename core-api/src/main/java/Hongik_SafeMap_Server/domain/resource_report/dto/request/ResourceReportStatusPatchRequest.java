package Hongik_SafeMap_Server.domain.resource_report.dto.request;

import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "자원 게시글 상태 변경 요청")
public record ResourceReportStatusPatchRequest(
        @Schema(description = "변경할 상태", example = "마감", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "상태는 필수입니다")
        ResourceReportStatus status
) {
}