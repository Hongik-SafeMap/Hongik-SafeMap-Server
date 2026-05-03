package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;

import java.util.List;

public record DisasterStatisticsSummaryResponse(
        long totalGroupCount,
        long totalReportCount,
        long blindedReportCount,
        double averageReportsPerGroup,
        DisasterTypeResponse mostFrequentDisasterType,
        List<DisasterTypeStatistics> disasterTypeStats,
        List<RiskLevelStatistics> riskLevelDistribution
) {
    public static DisasterStatisticsSummaryResponse of(
            long totalGroupCount,
            long totalReportCount,
            long blindedReportCount,
            double averageReportsPerGroup,
            DisasterTypeResponse mostFrequentDisasterType,
            List<DisasterTypeStatistics> disasterTypeStats,
            List<RiskLevelStatistics> riskLevelDistribution
    ) {
        return new DisasterStatisticsSummaryResponse(
                totalGroupCount,
                totalReportCount,
                blindedReportCount,
                averageReportsPerGroup,
                mostFrequentDisasterType,
                disasterTypeStats,
                riskLevelDistribution
        );
    }

    public record DisasterTypeStatistics(
            DisasterTypeResponse disasterType,
            long reportCount
    ) {
    }

    public record RiskLevelStatistics(
            RiskLevel riskLevel,
            long reportCount
    ) {
    }
}
