package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;

public record DisasterReportEvaluationResponse(
        int helpfulCount,
        int notHelpfulCount,
        int accurateCount,
        int falseReportCount,
        boolean userEvaluatedHelpful,
        boolean userEvaluatedNotHelpful,
        boolean userEvaluatedAccurate,
        boolean userEvaluatedFalseReport
) {

    public static DisasterReportEvaluationResponse of(DisasterReportEvaluation dre,
                                                      boolean userEvaluatedHelpful,
                                                      boolean userEvaluatedNotHelpful,
                                                      boolean userEvaluatedAccurate,
                                                      boolean userEvaluatedFalseReport) {
        return new DisasterReportEvaluationResponse(
                dre.getHelpfulCount(),
                dre.getNotHelpfulCount(),
                dre.getAccurateCount(),
                dre.getFalseReportCount(),
                userEvaluatedHelpful,
                userEvaluatedNotHelpful,
                userEvaluatedAccurate,
                userEvaluatedFalseReport
        );
    }
}
