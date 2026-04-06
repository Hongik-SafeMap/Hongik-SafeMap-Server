package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.admin.member.dto.AdminMemberResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    Page<DisasterReport> findByDisasterTypeInOrderByCreatedAtDesc(List<DisasterType> disasterTypes, Pageable pageable);

    // 긴급도별 필터링 조회
    Page<DisasterReport> findByRiskLevelInOrderByCreatedAtDesc(List<RiskLevel> riskLevels, Pageable pageable);

    // 재난 유형과 긴급도 모두 필터링 조회
    Page<DisasterReport> findByDisasterTypeInAndRiskLevelInOrderByCreatedAtDesc(List<DisasterType> disasterTypes, List<RiskLevel> riskLevels, Pageable pageable);

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
}
