package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.UserEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEvaluationRepository extends JpaRepository<UserEvaluation, Long> {
    
    Optional<UserEvaluation> findByMemberIdAndDisasterReportId(Long memberId, Long disasterReportId);
}