package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record DisasterSimulationResponse(
        Summary summary,
        List<Frame> frames
) {
    public static DisasterSimulationResponse of(DisasterReportGroup group, List<DisasterReport> reports) {
        List<DisasterReport> sorted = reports.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .toList();

        LocalDateTime startTime = sorted.isEmpty() ? null : sorted.get(0).getCreatedAt();
        LocalDateTime endTime = sorted.isEmpty() ? null : sorted.get(sorted.size() - 1).getCreatedAt();

        Summary summary = new Summary(
                DisasterTypeResponse.of(group.getDisasterType()),
                sorted.size(),
                startTime,
                endTime
        );

        List<Frame> frames = new java.util.ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            List<DisasterReport> soFar = sorted.subList(0, i + 1);
            frames.add(Frame.of(i + 1, soFar, startTime));
        }

        return new DisasterSimulationResponse(summary, frames);
    }

    public record Summary(
            DisasterTypeResponse disasterType,
            int totalReportCount,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {}

    public record Frame(
            int reportIndex,
            List<Location> locations,
            Statistics statistics
    ) {
        static Frame of(int index, List<DisasterReport> soFar, LocalDateTime startTime) {
            List<Location> locations = soFar.stream()
                    .map(Location::of)
                    .toList();

            DisasterReport current = soFar.get(soFar.size() - 1);
            Map<RiskLevel, Long> riskCounts = soFar.stream()
                    .collect(Collectors.groupingBy(DisasterReport::getRiskLevel, Collectors.counting()));
            Map<String, Long> riskLevelCounts = Arrays.stream(RiskLevel.values())
                    .collect(Collectors.toMap(RiskLevel::getDescription, r -> riskCounts.getOrDefault(r, 0L)));

            Statistics statistics = new Statistics(
                    current.getCreatedAt(),
                    soFar.size(),
                    riskLevelCounts
            );

            return new Frame(index, locations, statistics);
        }
    }

    public record Location(
            @JsonFormat(shape = JsonFormat.Shape.NUMBER) Double latitude,
            @JsonFormat(shape = JsonFormat.Shape.NUMBER) Double longitude,
            RiskLevel riskLevel
    ) {
        static Location of(DisasterReport report) {
            return new Location(report.getLatitude(), report.getLongitude(), report.getRiskLevel());
        }
    }

    public record Statistics(
            LocalDateTime reportTime,
            int cumulativeCount,
            Map<String, Long> riskLevelCounts
    ) {}
}
