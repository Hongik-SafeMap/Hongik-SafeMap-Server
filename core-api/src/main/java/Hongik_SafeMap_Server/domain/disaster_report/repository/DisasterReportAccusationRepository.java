package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportAccusation;
import Hongik_SafeMap_Server.global.dto.response.ReportAccusationCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisasterReportAccusationRepository extends JpaRepository<DisasterReportAccusation, Long> {

    boolean existsByMemberIdAndDisasterReportId(Long memberId, Long disasterReportId);

    Optional<DisasterReportAccusation> findByMemberIdAndDisasterReportId(Long memberId, Long disasterReportId);

    int countByDisasterReportId(Long disasterReportId);

    @Query("SELECT new Hongik_SafeMap_Server.global.dto.response.ReportAccusationCount(a.disasterReport.id, CAST(COUNT(a) AS int)) " +
            "FROM DisasterReportAccusation a " +
            "WHERE a.disasterReport.id IN :reportIds " +
            "GROUP BY a.disasterReport.id")
    List<ReportAccusationCount> countByReportIds(@Param("reportIds") List<Long> reportIds);
}