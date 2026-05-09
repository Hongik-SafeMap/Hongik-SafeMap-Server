package Hongik_SafeMap_Server.domain.admin.disaster_archive.service;

import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.request.UpdateGroupTitleRequest;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterRecordListResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse.DisasterTypeStatistics;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse.RiskLevelStatistics;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterSimulationResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.GroupLocationResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterArchiveRecordResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupDetailResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.repository.DisasterReportGroupRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        long blindedReportCount = disasterReportRepository.countReportsByStatusWithFilters(DisasterReportStatus.BLINDED, disasterTypeIds, from, to);
        long approvedReportCount = disasterTypeStats.stream().mapToLong(DisasterTypeStatistics::reportCount).sum();
        long totalReportCount = approvedReportCount + blindedReportCount;

        double averageReportsPerGroup = totalGroupCount == 0 ? 0.0
                : Math.round((double) approvedReportCount / totalGroupCount * 10.0) / 10.0;

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
                blindedReportCount,
                averageReportsPerGroup,
                mostFrequentDisasterType,
                disasterTypeStats,
                riskLevelDistribution
        );
    }

    public DisasterRecordListResponse getDisasterRecords(
            List<RiskLevel> riskLevels,
            LocalDate fromDate,
            LocalDate toDate,
            int page,
            int size
    ) {
        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime to = toDate != null ? toDate.atTime(LocalTime.MAX) : null;
        List<RiskLevel> riskLevelFilter = (riskLevels != null && riskLevels.isEmpty()) ? null : riskLevels;

        Pageable pageable = PageRequest.of(page, size);
        Page<DisasterReportGroup> result = disasterReportGroupRepository.findAllGroupsForArchive(
                riskLevelFilter, from, to, pageable);

        List<Long> groupIds = result.getContent().stream().map(DisasterReportGroup::getId).toList();

        // 그룹 ID 목록으로 상태별 제보 수 배치 조회 (N+1 방지)
        Map<Long, Map<DisasterReportStatus, Long>> statusCountsByGroup = disasterReportGroupRepository
                .countReportsByStatusForGroupIds(
                        groupIds,
                        List.of(DisasterReportStatus.APPROVED, DisasterReportStatus.PENDING))
                .stream()
                .collect(Collectors.groupingBy(
                        row -> (Long) row[0],
                        Collectors.toMap(row -> (DisasterReportStatus) row[1], row -> (Long) row[2])
                ));

        List<DisasterArchiveRecordResponse> records = result.getContent().stream()
                .map(group -> {
                    Map<DisasterReportStatus, Long> counts = statusCountsByGroup.getOrDefault(group.getId(), Map.of());
                    return new DisasterArchiveRecordResponse(
                            group.getId(),
                            group.getTitle(),
                            DisasterTypeResponse.of(group.getDisasterType()),
                            group.getCenterLatitude(),
                            group.getCenterLongitude(),
                            group.getEarliestReportTime(),
                            group.getLatestReportTime(),
                            group.getReportCount(),
                            counts.getOrDefault(DisasterReportStatus.APPROVED, 0L).intValue(),
                            counts.getOrDefault(DisasterReportStatus.PENDING, 0L).intValue(),
                            group.getLatestRiskLevel(),
                            group.getEarliestAddress()
                    );
                })
                .toList();

        return new DisasterRecordListResponse(
                records,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
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

    public GroupDetailResponse getGroupDetail(Long groupId) {
        return disasterReportGroupService.getGroupDetail(groupId);
    }

    @Transactional
    public void updateGroupTitle(Long groupId, UpdateGroupTitleRequest request) {
        DisasterReportGroup group = disasterReportGroupRepository.findById(groupId)
                .orElseThrow(() -> new DisasterReportException(ErrorMessage.DISASTER_REPORT_GROUP_NOT_FOUND));
        group.updateTitle(request.title());
    }
}
