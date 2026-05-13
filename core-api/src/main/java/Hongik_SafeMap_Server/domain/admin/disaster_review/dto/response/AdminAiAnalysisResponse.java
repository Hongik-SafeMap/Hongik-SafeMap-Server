package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminAiAnalysisResponse(
        @Schema(description = "AI 생성 이미지 확률", example = "0.82")
        Double aiGeneratedProbability,

        @Schema(description = "실제 이미지 확률", example = "0.18")
        Double realProbability,

        @Schema(description = "AI 이미지 판별 결과", example = "AI_GENERATED")
        String aiPrediction,

        @Schema(description = "정보성 있음 확률", example = "0.76")
        Double informativeProbability,

        @Schema(description = "정보성 없음 확률", example = "0.24")
        Double notInformativeProbability,

        @Schema(description = "정보성 판별 결과", example = "INFORMATIVE")
        String informativePrediction,

        @Schema(description = "최종 신뢰도 점수", example = "42")
        Integer trustScore
) {
}
