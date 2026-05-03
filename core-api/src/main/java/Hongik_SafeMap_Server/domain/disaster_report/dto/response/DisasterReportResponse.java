package Hongik_SafeMap_Server.domain.disaster_report.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;

public record DisasterReportResponse(
        Long id,
        DisasterTypeResponse disasterType,
        RiskLevel riskLevel,
        String disasterDescription,
        Double latitude,
        Double longitude,
        String address,
        List<String> fileUrls,
        DisasterReportStatus status,

        Double aiGeneratedProbability,
        Double realProbability,
        String aiPrediction,
        Double informativeProbability,
        Double notInformativeProbability,
        String informativePrediction,
        Integer trustScore,

        LocalDateTime createdAt,
        Long memberId
) {
    public static DisasterReportResponse of(DisasterReport dr) {
        return new DisasterReportResponse(
                dr.getId(),
                DisasterTypeResponse.of(dr.getDisasterType()),
                dr.getRiskLevel(),
                dr.getDisasterDescription(),
                dr.getLatitude(),
                dr.getLongitude(),
                dr.getAddress(),
                dr.getFileUrls(),
                dr.getStatus(),

                dr.getAiGeneratedProbability(),
                dr.getRealProbability(),
                dr.getAiPrediction(),
                dr.getInformativeProbability(),
                dr.getNotInformativeProbability(),
                dr.getInformativePrediction(),
                dr.getTrustScore(),

                dr.getCreatedAt(),
                dr.getMember().getId()
        );
    }
}
