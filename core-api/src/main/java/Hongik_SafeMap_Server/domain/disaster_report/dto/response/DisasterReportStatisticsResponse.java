package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportAccusation;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "재난 제보 통계 응답")
public class DisasterReportStatisticsResponse {
    
    @Schema(description = "재난 제보 ID", example = "1")
    private Long disasterReportId;
    
    @Schema(description = "총 도움됨 수", example = "25")
    private int helpfulCount;
    
    @Schema(description = "총 도움 안됨 수", example = "3")
    private int notHelpfulCount;
    
    @Schema(description = "총 신고 수", example = "2")
    private int accusationCount;

    public static DisasterReportStatisticsResponse of(
            Long disasterReportId,
            DisasterReportEvaluation evaluation,
            int accusationCount
    ) {
        return DisasterReportStatisticsResponse.builder()
                .disasterReportId(disasterReportId)
                .helpfulCount(evaluation != null ? evaluation.getHelpfulCount() : 0)
                .notHelpfulCount(evaluation != null ? evaluation.getNotHelpfulCount() : 0)
                .accusationCount(accusationCount)
                .build();
    }
}