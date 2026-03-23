package Hongik_SafeMap_Server.domain.lost_report.dto.request;

import Hongik_SafeMap_Server.vo.LostReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "실종신고 상태 변경 요청")
public record LostReportStatusPatchRequest(
        @Schema(description = "변경할 상태", example = "발견됨", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "상태는 필수입니다")
        LostReportStatus status
) {
}