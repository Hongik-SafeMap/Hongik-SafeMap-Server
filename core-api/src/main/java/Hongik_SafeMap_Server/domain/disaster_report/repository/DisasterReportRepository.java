package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.admin.member.dto.AdminMemberResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {

    // 긴급 제보 전체 조회
    Page<DisasterReport> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 마이페이지 - 내가 작성한 제보 목록
    Page<DisasterReport> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    // 전체 제보 수
    long count();

    // 관리자 대시보드 - 최신 제보 4개 가져오기
    List<DisasterReport> findTop4ByOrderByCreatedAtDesc();

    // 관리자 대시보드 - 블라인드된 제보 수 조회
    long countByStatus(DisasterReportStatus status);

    // 관리자 제보 검토 - 상태별 조회(승인/블라인드/허위)
    Page<DisasterReport> findByStatusOrderByCreatedAtDesc(DisasterReportStatus status, Pageable pageable);

    // 재난 유형별 필터링 조회
    Page<DisasterReport> findByDisasterTypeIdInOrderByCreatedAtDesc(List<Long> disasterTypeIds, Pageable pageable);

    // 긴급도별 필터링 조회
    Page<DisasterReport> findByRiskLevelInOrderByCreatedAtDesc(List<RiskLevel> riskLevels, Pageable pageable);

    // 재난 유형과 긴급도 모두 필터링 조회
    Page<DisasterReport> findByDisasterTypeIdInAndRiskLevelInOrderByCreatedAtDesc(List<Long> disasterTypeIds, List<RiskLevel> riskLevels, Pageable pageable);

    // 상태별 필터링 조회
    Page<DisasterReport> findByStatusInOrderByCreatedAtDesc(List<DisasterReportStatus> statuses, Pageable pageable);

    // 재난 유형과 상태 필터링 조회
    Page<DisasterReport> findByDisasterTypeIdInAndStatusInOrderByCreatedAtDesc(List<Long> disasterTypeIds, List<DisasterReportStatus> statuses, Pageable pageable);

    // 긴급도와 상태 필터링 조회
    Page<DisasterReport> findByRiskLevelInAndStatusInOrderByCreatedAtDesc(List<RiskLevel> riskLevels, List<DisasterReportStatus> statuses, Pageable pageable);

    // 재난 유형, 긴급도, 상태 모두 필터링 조회
    Page<DisasterReport> findByDisasterTypeIdInAndRiskLevelInAndStatusInOrderByCreatedAtDesc(List<Long> disasterTypeIds, List<RiskLevel> riskLevels, List<DisasterReportStatus> statuses, Pageable pageable);

    @Query("""
                select new Hongik_SafeMap_Server.domain.admin.member.dto.AdminMemberResponse(
                    m.id,
                    m.name,
                    m.email,
                    count(dr),
                    0,
                    m.isCredible
                )
                from Member m
                left join DisasterReport dr on dr.member = m
                where m.status = Hongik_SafeMap_Server.vo.MemberStatus.USER
                group by m.id, m.name, m.email, m.isCredible
            """)
    List<AdminMemberResponse> findAdminMemberList();

    // 그룹 아이디로 제보 목록 조회
    List<DisasterReport> findByGroupId(@Param("groupId") Long groupId);

    // 여러 그룹의 제보들 조회
    @Query("SELECT dr FROM DisasterReport dr WHERE dr.group.id IN :groupIds")
    List<DisasterReport> findByGroupIdIn(@Param("groupIds") List<Long> groupIds);

    // 통계 - 필터 적용 재난 그룹 수 (중복 제거)
    @Query("SELECT COUNT(DISTINCT dr.group.id) FROM DisasterReport dr " +
            "WHERE dr.group IS NOT NULL " +
            "AND (:disasterTypeIds IS NULL OR dr.disasterType.id IN :disasterTypeIds) " +
            "AND (:from IS NULL OR dr.createdAt >= :from) " +
            "AND (:to IS NULL OR dr.createdAt <= :to)")
    long countDistinctGroupsWithFilters(
            @Param("disasterTypeIds") List<Long> disasterTypeIds,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // 통계 - 재난 유형별 제보 수 (필터 포함, 재난 유형 ID 오름차순)
    @Query("SELECT dr.disasterType, COUNT(dr) FROM DisasterReport dr " +
            "WHERE (:disasterTypeIds IS NULL OR dr.disasterType.id IN :disasterTypeIds) " +
            "AND (:from IS NULL OR dr.createdAt >= :from) " +
            "AND (:to IS NULL OR dr.createdAt <= :to) " +
            "GROUP BY dr.disasterType ORDER BY dr.disasterType.id ASC")
    List<Object[]> countReportsByDisasterTypeWithFilters(
            @Param("disasterTypeIds") List<Long> disasterTypeIds,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // 통계 - 심각도별 제보 수 (필터 포함)
    @Query("SELECT dr.riskLevel, COUNT(dr) FROM DisasterReport dr " +
            "WHERE (:disasterTypeIds IS NULL OR dr.disasterType.id IN :disasterTypeIds) " +
            "AND (:from IS NULL OR dr.createdAt >= :from) " +
            "AND (:to IS NULL OR dr.createdAt <= :to) " +
            "GROUP BY dr.riskLevel")
    List<Object[]> countReportsByRiskLevelWithFilters(
            @Param("disasterTypeIds") List<Long> disasterTypeIds,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
