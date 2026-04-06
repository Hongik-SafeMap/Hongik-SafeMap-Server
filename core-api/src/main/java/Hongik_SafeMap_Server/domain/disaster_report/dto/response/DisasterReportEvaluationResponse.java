package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;

public record DisasterReportEvaluationResponse(
        int helpfulCount,
        int notHelpfulCount,
        boolean userEvaluatedHelpful,
        boolean userEvaluatedNotHelpful
) {

    public static DisasterReportEvaluationResponse of(DisasterReportEvaluation dre,
                                                      boolean userEvaluatedHelpful,
                                                      boolean userEvaluatedNotHelpful) {
        return new DisasterReportEvaluationResponse(
                dre.getHelpfulCount(),
                dre.getNotHelpfulCount(),
                userEvaluatedHelpful,
                userEvaluatedNotHelpful
        );
    }

    public static DisasterReportEvaluationResponse ofDefault() {
        return new DisasterReportEvaluationResponse(
                0,
                0,
                false,
                false
        );
    }
}
