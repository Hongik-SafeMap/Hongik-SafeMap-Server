package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportAccusation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisasterReportAccusationRepository extends JpaRepository<DisasterReportAccusation, Long> {
    
    boolean existsByMemberIdAndDisasterReportId(Long memberId, Long disasterReportId);
    
    Optional<DisasterReportAccusation> findByMemberIdAndDisasterReportId(Long memberId, Long disasterReportId);
    
    long countByDisasterReportId(Long disasterReportId);
}