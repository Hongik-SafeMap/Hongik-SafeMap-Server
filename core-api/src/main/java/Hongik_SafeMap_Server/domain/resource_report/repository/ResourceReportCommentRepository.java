package Hongik_SafeMap_Server.domain.resource_report.repository;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceReportCommentRepository extends JpaRepository<ResourceReportComment, Long> {
    List<ResourceReportComment> findByResourceReportIdOrderByCreatedAtAsc(Long resourceReportId);
    
    long countByResourceReportId(Long resourceReportId);
}