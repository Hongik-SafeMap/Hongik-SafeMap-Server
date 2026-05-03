package Hongik_SafeMap_Server.domain.admin.disaster_archive.service;

import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterRecordListResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse.DisasterTypeStatistics;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse.RiskLevelStatistics;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterSimulationResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.GroupLocationResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupedDisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.repository.DisasterReportGroupRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.vo.RiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDisasterArchiveService {

    private final DisasterReportRepository disasterReportRepository;
    private final DisasterReportGroupRepository disasterReportGroupRepository;
    private final DisasterReportGroupService disasterReportGroupService;

    public DisasterStatisticsSummaryResponse getStatisticsSummary(
            List<Long> disasterTypeIds,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime to = toDate != null ? toDate.atTime(LocalTime.MAX) : null;

        List<DisasterTypeStatistics> disasterTypeStats = disasterReportRepository
                .countReportsByDisasterTypeWithFilters(disasterTypeIds, from, to)
                .stream()
                .map(row -> new DisasterTypeStatistics(
                        DisasterTypeResponse.of((DisasterType) row[0]),
                        (Long) row[1]
                ))
                .toList();

        long totalGroupCount = disasterReportRepository.countDistinctGroupsWithFilters(disasterTypeIds, from, to);
        long totalReportCount = disasterTypeStats.stream().mapToLong(DisasterTypeStatistics::reportCount).sum();

        double averageReportsPerGroup = totalGroupCount == 0 ? 0.0
                : Math.round((double) totalReportCount / totalGroupCount * 10.0) / 10.0;

        DisasterTypeResponse mostFrequentDisasterType = disasterTypeStats.stream()
                .max(Comparator.comparingLong(DisasterTypeStatistics::reportCount))
                .map(DisasterTypeStatistics::disasterType)
                .orElse(null);

        List<RiskLevelStatistics> riskLevelDistribution = disasterReportRepository
                .countReportsByRiskLevelWithFilters(disasterTypeIds, from, to)
                .stream()
                .map(row -> new RiskLevelStatistics(
                        (RiskLevel) row[0],
                        (Long) row[1]
                ))
                .toList();

        return DisasterStatisticsSummaryResponse.of(
                totalGroupCount,
                totalReportCount,
                averageReportsPerGroup,
                mostFrequentDisasterType,
                disasterTypeStats,
                riskLevelDistribution
        );
    }

    public DisasterRecordListResponse getDisasterRecords() {
        List<GroupedDisasterReportResponse> records =
                disasterReportGroupService.getGroupedReports(null, null, 0, false, null, null);
        return DisasterRecordListResponse.of(records);
    }

    public GroupLocationResponse getGroupLocation(Long groupId) {
        DisasterReportGroup group = disasterReportGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DISASTER_REPORT_GROUP_NOT_FOUND));
        List<DisasterReport> reports = disasterReportRepository.findByGroupId(groupId);
        return GroupLocationResponse.of(group, reports);
    }

    public DisasterSimulationResponse getDisasterSimulation(Long groupId) {
        DisasterReportGroup group = disasterReportGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DISASTER_REPORT_GROUP_NOT_FOUND));
        List<DisasterReport> reports = disasterReportRepository.findByGroupId(groupId);
        return DisasterSimulationResponse.of(group, reports);
    }
}
