package Hongik_SafeMap_Server.domain.resource_report.repository;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceReportRepository extends JpaRepository<ResourceReport, Long> {
    
    @Query("SELECT rr FROM ResourceReport rr WHERE rr.id = :id AND rr.deletedAt IS NULL")
    Optional<ResourceReport> findByIdAndNotDeleted(@Param("id") Long id);
}