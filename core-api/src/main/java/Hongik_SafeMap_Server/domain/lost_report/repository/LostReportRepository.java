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

import java.util.Collection;
import java.util.Optional;

@Repository
public interface LostReportRepository extends JpaRepository<LostReport, Long> {

    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
            "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
            "WHERE lr.deletedAt IS NULL AND lr.category IN :categories AND lr.status IN :statuses " +
            "GROUP BY lr")
    Page<LostReportWithCommentCount> findWithFilters(
            @Param("categories") Collection<LostReportCategory> categories,
            @Param("statuses") Collection<LostReportStatus> statuses,
            Pageable pageable);

    // 내가 작성한 실종 신고 조회 (삭제되지 않은 것만, 최신순, 페이징)
    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount(lr, COUNT(lrc)) " +
            "FROM LostReport lr LEFT JOIN LostReportComment lrc ON lr.id = lrc.lostReport.id " +
            "WHERE lr.deletedAt IS NULL AND lr.member.id = :memberId " +
            "GROUP BY lr")
    Page<LostReportWithCommentCount> findByMemberIdWithCommentCount(@Param("memberId") Long memberId, Pageable pageable);

    // 삭제되지 않은 게시물만 조회
    @Query("SELECT lr FROM LostReport lr WHERE lr.id = :id AND lr.deletedAt IS NULL")
    Optional<LostReport> findByIdAndNotDeleted(@Param("id") Long id);
}