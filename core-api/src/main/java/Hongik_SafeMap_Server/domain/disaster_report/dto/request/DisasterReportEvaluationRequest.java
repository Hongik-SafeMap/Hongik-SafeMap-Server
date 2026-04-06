package Hongik_SafeMap_Server.domain.disaster_report.dto.request;

import Hongik_SafeMap_Server.vo.DisasterReportEvaluationType;
import jakarta.validation.constraints.NotNull;

public record DisasterReportEvaluationRequest(
        @NotNull(message = "평가 타입은 필수입니다.")
        DisasterReportEvaluationType evaluationType
) {
}