package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.request;

import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DisasterReportStatusUpdateRequest(
        @Schema(description = "변경할 상태", example = "승인")
        @NotNull(message = "상태는 필수입니다")
        DisasterReportStatus status,

        @Schema(description = "검토 의견", example = "~~에 따라 신뢰할 수 있는 제보로 판단됩니다.")
        String reviewComment
) {
}