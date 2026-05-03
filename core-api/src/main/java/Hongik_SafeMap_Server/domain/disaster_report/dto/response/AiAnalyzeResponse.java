package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

public record AiAnalyzeResponse(
        Long reportId,
        Double aiGeneratedProbability,
        Double realProbability,
        String aiPrediction,
        Double informativeProbability,
        Double notInformativeProbability,
        String informativePrediction,
        Integer trustScore,
        String status
) {}
