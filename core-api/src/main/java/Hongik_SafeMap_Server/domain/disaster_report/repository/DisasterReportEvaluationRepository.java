package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisasterReportEvaluationRepository extends JpaRepository<DisasterReportEvaluation, Long> {

    DisasterReportEvaluation findDisasterReportEvaluationById(Long id);
}