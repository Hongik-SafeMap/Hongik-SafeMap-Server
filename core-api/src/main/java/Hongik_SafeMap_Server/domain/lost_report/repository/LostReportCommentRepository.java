package Hongik_SafeMap_Server.domain.lost_report.repository;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostReportCommentRepository extends JpaRepository<LostReportComment, Long> {
    List<LostReportComment> findByLostReportIdOrderByCreatedAtAsc(Long lostReportId);
    
    // 게시글별 댓글 개수 조회
    long countByLostReportId(Long lostReportId);
}