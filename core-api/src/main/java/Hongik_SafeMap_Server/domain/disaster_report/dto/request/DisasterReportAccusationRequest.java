package Hongik_SafeMap_Server.domain.disaster_report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "재난 제보 신고 요청")
public class DisasterReportAccusationRequest {
    
    @NotNull(message = "재난 제보 ID는 필수입니다")
    @Schema(description = "재난 제보 ID", example = "1")
    private Long disasterReportId;
}