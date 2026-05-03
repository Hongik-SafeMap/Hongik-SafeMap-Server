package Hongik_SafeMap_Server.domain.disaster_report_group.repository;

import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DisasterReportGroupRepository extends JpaRepository<DisasterReportGroup, Long> {

    // 활성 상태인 그룹들 조회
    List<DisasterReportGroup> findByIsActiveTrueOrderByLatestReportTimeDesc();

    // 특정 재난 타입에서 활성 상태인 그룹 찾기 (새로운 제보가 들어왔을 때 매칭용)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "WHERE drg.isActive = true " +
            "AND drg.disasterType = :disasterType " +
            "ORDER BY drg.latestReportTime DESC")
    List<DisasterReportGroup> findActiveGroupsForMatching(
            @Param("disasterType") DisasterType disasterType);

    // 모든 그룹(활성+비활성) 찾기 (블라인드 해제 제보 재배치용)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "JOIN FETCH drg.disasterType " +
            "WHERE drg.disasterType = :disasterType " +
            "ORDER BY drg.latestReportTime DESC")
    List<DisasterReportGroup> findAllGroupsForReAssignment(
            @Param("disasterType") DisasterType disasterType);

    // 특정 시간 이전에 마지막 업데이트된 그룹들 (비활성화 대상)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "WHERE drg.isActive = true " +
            "AND drg.latestReportTime < :cutoffTime")
    List<DisasterReportGroup> findGroupsToDeactivate(@Param("cutoffTime") LocalDateTime cutoffTime);

    // 활성 그룹들의 요약 정보 조회 (실시간 지도용)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "JOIN FETCH drg.disasterType " +
            "WHERE drg.isActive = true " +
            "AND drg.reportCount > 0 " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findActiveGroupsSummary();

    // 모든 그룹들의 요약 정보 조회 (활성/비활성 포함)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "JOIN FETCH drg.disasterType " +
            "WHERE drg.reportCount > 0 " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findAllGroupsSummary();

    // 활성 그룹들의 요약 정보 조회 (필터링 포함)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "JOIN FETCH drg.disasterType " +
            "WHERE drg.isActive = true " +
            "AND drg.reportCount > 0 " +
            "AND (:disasterTypeIds IS NULL OR drg.disasterType.id IN :disasterTypeIds) " +
            "AND (:riskLevels IS NULL OR drg.latestRiskLevel IN :riskLevels) " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findActiveGroupsWithFilters(
            @Param("disasterTypeIds") List<Long> disasterTypeIds,
            @Param("riskLevels") List<RiskLevel> riskLevels);

    // 모든 그룹들의 요약 정보 조회 (필터링 포함)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "JOIN FETCH drg.disasterType " +
            "WHERE drg.reportCount > 0 " +
            "AND (:disasterTypeIds IS NULL OR drg.disasterType.id IN :disasterTypeIds) " +
            "AND (:riskLevels IS NULL OR drg.latestRiskLevel IN :riskLevels) " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findAllGroupsWithFilters(
            @Param("disasterTypeIds") List<Long> disasterTypeIds,
            @Param("riskLevels") List<RiskLevel> riskLevels);

    // 재난 기록 아카이브 - 페이지네이션 + riskLevel/날짜 필터
    @Query(value = "SELECT drg FROM DisasterReportGroup drg JOIN FETCH drg.disasterType " +
            "WHERE drg.reportCount > 0 " +
            "AND (:riskLevels IS NULL OR drg.latestRiskLevel IN :riskLevels) " +
            "AND (:from IS NULL OR drg.earliestReportTime >= :from) " +
            "AND (:to IS NULL OR drg.earliestReportTime <= :to) " +
            "ORDER BY drg.earliestReportTime DESC",
            countQuery = "SELECT COUNT(drg) FROM DisasterReportGroup drg " +
            "WHERE drg.reportCount > 0 " +
            "AND (:riskLevels IS NULL OR drg.latestRiskLevel IN :riskLevels) " +
            "AND (:from IS NULL OR drg.earliestReportTime >= :from) " +
            "AND (:to IS NULL OR drg.earliestReportTime <= :to)")
    Page<DisasterReportGroup> findAllGroupsForArchive(
            @Param("riskLevels") List<RiskLevel> riskLevels,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    // 아카이브 - 그룹 ID 목록으로 상태별 제보 수 배치 조회
    @Query("SELECT dr.group.id, dr.status, COUNT(dr) FROM DisasterReport dr " +
            "WHERE dr.group.id IN :groupIds AND dr.status IN :statuses " +
            "GROUP BY dr.group.id, dr.status")
    List<Object[]> countReportsByStatusForGroupIds(
            @Param("groupIds") List<Long> groupIds,
            @Param("statuses") List<DisasterReportStatus> statuses);

    // 통계 요약 - 그룹별 평균 제보 수
    @Query("SELECT AVG(drg.reportCount) FROM DisasterReportGroup drg")
    Double findAverageReportCount();

    // 통계 요약 - 가장 많이 등록된 재난 유형 (그룹 수 기준)
    @Query("SELECT drg.disasterType FROM DisasterReportGroup drg " +
            "GROUP BY drg.disasterType " +
            "ORDER BY COUNT(drg) DESC")
    List<DisasterType> findMostFrequentDisasterTypes(Pageable pageable);
}
