package Hongik_SafeMap_Server.domain.disaster_report_group.repository;

import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.vo.DisasterType;
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

    // 특정 시간 이전에 마지막 업데이트된 그룹들 (비활성화 대상)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "WHERE drg.isActive = true " +
            "AND drg.latestReportTime < :cutoffTime")
    List<DisasterReportGroup> findGroupsToDeactivate(@Param("cutoffTime") LocalDateTime cutoffTime);

    // 활성 그룹들의 요약 정보 조회 (실시간 지도용)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "WHERE drg.isActive = true " +
            "AND drg.reportCount > 0 " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findActiveGroupsSummary();

    // 모든 그룹들의 요약 정보 조회 (활성/비활성 포함)
    @Query("SELECT drg FROM DisasterReportGroup drg " +
            "WHERE drg.reportCount > 0 " +
            "ORDER BY drg.reportCount DESC, drg.latestReportTime DESC")
    List<DisasterReportGroup> findAllGroupsSummary();
}