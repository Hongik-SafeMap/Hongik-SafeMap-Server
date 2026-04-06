package Hongik_SafeMap_Server.domain.disaster_report_group.service;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupDetailResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupedDisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.repository.DisasterReportGroupRepository;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.util.DistanceUtil;
import Hongik_SafeMap_Server.vo.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisasterReportGroupService {

    // 그룹핑 기준 설정
    private static final int MAX_DISTANCE_M = 500; // 500m 이내
    private static final int MAX_TIME_SPAN_HOURS = 24; // 24시간 이내 (새 제보가 24시간 이후에 들어오면 새로운 그룹에 추가)
    private static final int GROUP_DEACTIVATE_HOURS = 24; // 마지막 제보로부터 24시간 후 비활성화(비활성화 되면 그룹이 지도에서 사라짐)
    private final DisasterReportGroupRepository groupRepository;
    private final DisasterReportRepository reportRepository;

    /**
     * 새로운 제보를 기존 그룹에 할당하거나 새 그룹을 생성
     */
    @Transactional
    public DisasterReportGroup assignReportToGroup(DisasterReport report) {
        log.info("제보 그룹핑 시작: reportId={}, type={}, location={}",
                report.getId(), report.getDisasterType(), report.getAddress());

        // 1. 기존 그룹에서 매칭 가능한 그룹 찾기 (거리, 시간 기반)
        Optional<DisasterReportGroup> matchingGroup = findMatchingGroup(report);

        if (matchingGroup.isPresent()) {
            // 2-1. 기존 그룹에 추가 (증분 통계 업데이트)
            DisasterReportGroup group = matchingGroup.get();

            // 제보 추가
            group.addReport(report);

            // 증분 통계 계산
            int newCount = group.getReportCount() + 1;
            double newCenterLat = (group.getCenterLatitude() * group.getReportCount() + report.getLatitude()) / newCount;
            double newCenterLng = (group.getCenterLongitude() * group.getReportCount() + report.getLongitude()) / newCount;
            LocalDateTime newLatestTime = report.getCreatedAt();
            RiskLevel newLatestRisk = report.getRiskLevel();

            // 통계 업데이트 (증분)
            group.updateStatistics(newCenterLat, newCenterLng, group.getEarliestReportTime(),
                    newLatestTime, newCount, newLatestRisk, true);
            DisasterReportGroup savedGroup = groupRepository.save(group);

            log.info("기존 그룹에 제보 추가: groupId={}, reportId={}", group.getId(), report.getId());
            return savedGroup;
        } else {
            // 2-2. 새 그룹 생성
            DisasterReportGroup newGroup = createNewGroup(report);
            newGroup.addReport(report);  // 그룹-제보 관계 설정
            DisasterReportGroup savedGroup = groupRepository.save(newGroup);

            log.info("새 그룹 생성: groupId={}, reportId={}", savedGroup.getId(), report.getId());
            return savedGroup;
        }
    }

    /**
     * 새로운 제보와 매칭되는 활성 그룹 찾기
     */
    private Optional<DisasterReportGroup> findMatchingGroup(DisasterReport report) {
        List<DisasterReportGroup> candidateGroups = groupRepository
                .findActiveGroupsForMatching(report.getDisasterType());

        return candidateGroups.stream()
                .filter(group -> group.canAcceptReport(report, MAX_DISTANCE_M, MAX_TIME_SPAN_HOURS))
                .findFirst();
    }

    /**
     * 새로운 그룹 생성 (첫 번째 제보 기반으로 초기화)
     */
    private DisasterReportGroup createNewGroup(DisasterReport report) {
        return DisasterReportGroup.createFromFirstReport(report);
    }

    /**
     * 오래된 그룹들 비활성화 (스케줄링)
     * 1시간마다 실행하여 24시간 이상 업데이트되지 않은 그룹을 비활성화
     */
    @Scheduled(fixedRate = 3600000) // 1시간마다 실행 (3600000ms = 1시간)
    @Transactional
    public void deactivateOldGroups() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(GROUP_DEACTIVATE_HOURS);
        List<DisasterReportGroup> oldGroups = groupRepository.findGroupsToDeactivate(cutoffTime);

        oldGroups.forEach(DisasterReportGroup::deactivate);
        groupRepository.saveAll(oldGroups);

        log.info("오래된 그룹 비활성화 완료: {} 개 그룹", oldGroups.size());
    }

    /**
     * 활성 그룹 목록 조회 (지도 표시용)
     */
    public List<DisasterReportGroup> getActiveGroups() {
        return groupRepository.findActiveGroupsSummary();
    }

    /**
     * 그룹 목록 조회 (활성 여부에 따라)
     */
    public List<DisasterReportGroup> getGroups(boolean activeOnly) {
        return activeOnly ? groupRepository.findActiveGroupsSummary() : groupRepository.findAllGroupsSummary();
    }


    /**
     * 그룹 통계 업데이트 (처음부터 재계산, 제보 제거 후)
     */
    @Transactional
    public void calculateGroupStatistics(Long groupId) {
        DisasterReportGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다: " + groupId));

        List<DisasterReport> approvedReports = reportRepository.findByGroupId(groupId);

        if (approvedReports.isEmpty()) {
            groupRepository.delete(group);
            return;
        }

        // 통계 재계산
        double avgLat = approvedReports.stream().mapToDouble(DisasterReport::getLatitude).average().orElse(0.0);
        double avgLng = approvedReports.stream().mapToDouble(DisasterReport::getLongitude).average().orElse(0.0);

        LocalDateTime earliestTime = approvedReports.stream()
                .map(DisasterReport::getCreatedAt)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime latestTime = approvedReports.stream()
                .map(DisasterReport::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        RiskLevel latestRisk = approvedReports.stream()
                .max(Comparator.comparing(DisasterReport::getCreatedAt))
                .map(DisasterReport::getRiskLevel)
                .orElse(RiskLevel.LOW);

        long hoursDiff = java.time.Duration.between(latestTime, LocalDateTime.now()).toHours();
        boolean isActive = hoursDiff < GROUP_DEACTIVATE_HOURS;

        group.updateStatistics(avgLat, avgLng, earliestTime, latestTime, approvedReports.size(), latestRisk, isActive);
    }

    // 지역별/재난유형별 그룹화 조회 (지도 클러스터링용) - 그룹 테이블 기반
    @Transactional(readOnly = true)
    public List<GroupedDisasterReportResponse> getGroupedReports(Double userLatitude, Double userLongitude, int radiusMeters, Boolean isActive) {
        List<DisasterReportGroup> groups = getGroups(isActive);

        // 사용자 위치가 제공된 경우 거리 필터링
        if (userLatitude != null && userLongitude != null) {
            groups = groups.stream()
                    .filter(group -> DistanceUtil.isWithinRadius(
                            userLatitude, userLongitude,
                            group.getCenterLatitude(), group.getCenterLongitude(),
                            radiusMeters))
                    .toList();
        }

        // 모든 그룹 ID를 수집
        List<Long> groupIds = groups.stream()
                .map(DisasterReportGroup::getId)
                .toList();

        List<DisasterReport> allReports = reportRepository.findByGroupIdIn(groupIds);

        // 그룹별로 제보들을 분류
        Map<Long, List<DisasterReport>> reportsByGroup = allReports.stream()
                .collect(Collectors.groupingBy(report -> report.getGroup().getId()));

        // 각 그룹을 응답 객체로 변환
        return groups.stream()
                .map(group -> new GroupedDisasterReportResponse(
                        group.getId(),
                        group.getDisasterType(),
                        group.getCenterLatitude(),
                        group.getCenterLongitude(),
                        group.getEarliestReportTime(),
                        group.getLatestReportTime(),
                        group.getReportCount(),
                        group.getLatestRiskLevel()
                ))
                .toList();
    }

    public GroupDetailResponse getGroupDetail(Long groupId) {
        DisasterReportGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DISASTER_REPORT_GROUP_NOT_FOUND));

        List<DisasterReport> reports = reportRepository.findByGroupId(groupId);
        List<DisasterReportListResponse> reportResponses = reports.stream()
                .map(DisasterReportListResponse::of)
                .toList();

        return new GroupDetailResponse(
                group.getId(),
                group.getDisasterType(),
                group.getCenterLatitude(),
                group.getCenterLongitude(),
                group.getEarliestReportTime(),
                group.getLatestReportTime(),
                group.getReportCount(),
                group.getLatestRiskLevel(),
                group.isActive(),
                reportResponses
        );
    }
}