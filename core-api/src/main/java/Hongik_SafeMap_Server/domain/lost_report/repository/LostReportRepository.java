package Hongik_SafeMap_Server.domain.lost_report.repository;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReport;
import Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount;
import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostReportRepository extends JpaRepository<LostReport, Long> {
    

    // 댓글 개수와 함께 전체 목록 조회 (최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
           "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
           "GROUP BY lr")
    Page<LostReportWithCommentCount> findAllWithCommentCount(Pageable pageable);

    // 댓글 개수와 함께 카테고리별 조회 (최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
           "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
           "WHERE lr.category = :category " +
           "GROUP BY lr")
    Page<LostReportWithCommentCount> findByCategoryWithCommentCount(@Param("category") LostReportCategory category, Pageable pageable);

    // 댓글 개수와 함께 상태별 조회 (최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
           "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
           "WHERE lr.status = :status " +
           "GROUP BY lr")
    Page<LostReportWithCommentCount> findByStatusWithCommentCount(@Param("status") LostReportStatus status, Pageable pageable);

    // 댓글 개수와 함께 카테고리 + 상태별 조회 (최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
           "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
           "WHERE lr.category = :category AND lr.status = :status " +
           "GROUP BY lr")
    Page<LostReportWithCommentCount> findByCategoryAndStatusWithCommentCount(
            @Param("category") LostReportCategory category, 
            @Param("status") LostReportStatus status, 
            Pageable pageable);
}