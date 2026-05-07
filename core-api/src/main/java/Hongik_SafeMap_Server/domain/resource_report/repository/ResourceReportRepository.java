package Hongik_SafeMap_Server.domain.resource_report.repository;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount;
import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import Hongik_SafeMap_Server.vo.ResourceReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ResourceReportRepository extends JpaRepository<ResourceReport, Long> {

    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.type IN :types AND rr.category IN :categories AND rr.status IN :statuses " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findWithFilters(
            @Param("types") Collection<ResourceReportType> types,
            @Param("categories") Collection<ResourceReportCategory> categories,
            @Param("statuses") Collection<ResourceReportStatus> statuses,
            Pageable pageable);

    // 내가 작성한 자원 게시물 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.member.id = :memberId " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByMemberIdWithCommentCount(@Param("memberId") Long memberId, Pageable pageable);

    // 삭제되지 않은 게시물만 조회
    @Query("SELECT rr FROM ResourceReport rr LEFT JOIN FETCH rr.member WHERE rr.id = :id AND rr.deletedAt IS NULL")
    Optional<ResourceReport> findByIdAndNotDeleted(@Param("id") Long id);
}