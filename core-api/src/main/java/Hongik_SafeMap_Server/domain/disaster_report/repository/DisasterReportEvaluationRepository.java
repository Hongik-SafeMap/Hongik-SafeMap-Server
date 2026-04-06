package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisasterReportEvaluationRepository extends JpaRepository<DisasterReportEvaluation, Long> {

    Optional<DisasterReportEvaluation> findDisasterReportEvaluationById(Long id);

    @Query("SELECT e FROM DisasterReportEvaluation e WHERE e.id IN :reportIds")
    List<DisasterReportEvaluation> findAllByReportIds(@Param("reportIds") List<Long> reportIds);
}