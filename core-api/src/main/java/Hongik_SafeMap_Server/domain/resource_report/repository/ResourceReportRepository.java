package Hongik_SafeMap_Server.domain.resource_report.repository;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceReportRepository extends JpaRepository<ResourceReport, Long> {
}