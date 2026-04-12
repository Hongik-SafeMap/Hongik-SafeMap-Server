package Hongik_SafeMap_Server.domain.admin.disaster_review.service;

import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response.AdminReportResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response.AdminReportPageResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportAccusationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {
    private final DisasterReportRepository disasterReportRepository;
    private final DisasterReportEvaluationRepository evaluationRepository;
    private final DisasterReportAccusationRepository accusationRepository;

    // 제보 검토 - 전체 제보 목록 (제보 평가 및 신고수 포함)
    public AdminReportPageResponse findAllReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DisasterReport> pageResult = disasterReportRepository.findAll(pageable);

        List<DisasterReport> reports = pageResult.getContent();
        List<Long> reportIds = reports.stream()
                .map(DisasterReport::getId)
                .collect(Collectors.toList());

        // 평가 통계 배치 조회
        Map<Long, DisasterReportEvaluation> evaluationMap = evaluationRepository.findAllByReportIds(reportIds)
                .stream()
                .collect(Collectors.toMap(DisasterReportEvaluation::getId, Function.identity()));

        // 신고 통계 배치 조회
        Map<Long, Integer> accusationCountMap = accusationRepository.countByReportIds(reportIds)
                .stream()
                .collect(Collectors.toMap(
                        result -> result.reportId(),
                        result -> result.count()
                ));

        // 통계와 함께 응답 생성
        List<AdminReportResponse> reportsWithStatistics = reports.stream()
                .map(report -> {
                    Long reportId = report.getId();
                    DisasterReportEvaluation evaluation = evaluationMap.get(reportId);
                    int helpfulCount = evaluation != null ? evaluation.getHelpfulCount() : 0;
                    int notHelpfulCount = evaluation != null ? evaluation.getNotHelpfulCount() : 0;
                    int accusationCount = accusationCountMap.getOrDefault(reportId, 0);

                    return AdminReportResponse.of(report, helpfulCount, notHelpfulCount, accusationCount);
                })
                .collect(Collectors.toList());

        return new AdminReportPageResponse(
                reportsWithStatistics,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
    }
}
