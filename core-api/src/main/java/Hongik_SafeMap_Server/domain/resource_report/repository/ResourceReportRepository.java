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

import java.util.Optional;

@Repository
public interface ResourceReportRepository extends JpaRepository<ResourceReport, Long> {

    // 댓글 개수와 함께 전체 목록 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findAllWithCommentCount(Pageable pageable);

    // 댓글 개수와 함께 유형별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.type = :type " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByTypeWithCommentCount(@Param("type") ResourceReportType type, Pageable pageable);

    // 댓글 개수와 함께 카테고리별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.category = :category " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByCategoryWithCommentCount(@Param("category") ResourceReportCategory category, Pageable pageable);

    // 댓글 개수와 함께 상태별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.status = :status " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByStatusWithCommentCount(@Param("status") ResourceReportStatus status, Pageable pageable);

    // 댓글 개수와 함께 유형 + 카테고리별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.type = :type AND rr.category = :category " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByTypeAndCategoryWithCommentCount(
            @Param("type") ResourceReportType type,
            @Param("category") ResourceReportCategory category,
            Pageable pageable);

    // 댓글 개수와 함께 유형 + 상태별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.type = :type AND rr.status = :status " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByTypeAndStatusWithCommentCount(
            @Param("type") ResourceReportType type,
            @Param("status") ResourceReportStatus status,
            Pageable pageable);

    // 댓글 개수와 함께 카테고리 + 상태별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.category = :category AND rr.status = :status " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByCategoryAndStatusWithCommentCount(
            @Param("category") ResourceReportCategory category,
            @Param("status") ResourceReportStatus status,
            Pageable pageable);

    // 댓글 개수와 함께 유형 + 카테고리 + 상태별 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount(rr, COUNT(rrc)) " +
            "FROM ResourceReport rr LEFT JOIN FETCH rr.member LEFT JOIN ResourceReportComment rrc ON rr.id = rrc.resourceReport.id " +
            "WHERE rr.deletedAt IS NULL AND rr.type = :type AND rr.category = :category AND rr.status = :status " +
            "GROUP BY rr")
    Page<ResourceReportWithCommentCount> findByTypeAndCategoryAndStatusWithCommentCount(
            @Param("type") ResourceReportType type,
            @Param("category") ResourceReportCategory category,
            @Param("status") ResourceReportStatus status,
            Pageable pageable);

    // 삭제되지 않은 게시물만 조회
    @Query("SELECT rr FROM ResourceReport rr LEFT JOIN FETCH rr.member WHERE rr.id = :id AND rr.deletedAt IS NULL")
    Optional<ResourceReport> findByIdAndNotDeleted(@Param("id") Long id);
}