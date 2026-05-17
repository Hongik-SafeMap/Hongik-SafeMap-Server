package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response;

public record AdminAiAnalysisResponse(
        Double aiGeneratedProbability,
        Double realProbability,
        String aiPrediction,
        Double informativeProbability,
        Double notInformativeProbability,
        String informativePrediction,
        Integer trustScore,
        String status
) {
}
