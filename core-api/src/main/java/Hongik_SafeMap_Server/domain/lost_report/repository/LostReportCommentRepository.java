package Hongik_SafeMap_Server.domain.lost_report.repository;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostReportCommentRepository extends JpaRepository<LostReportComment, Long> {
}