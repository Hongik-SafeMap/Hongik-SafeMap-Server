package Hongik_SafeMap_Server.domain.admin.disaster_review.service;

import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.request.DisasterReportStatusUpdateRequest;
import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response.AdminReportPageResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response.AdminReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportAccusationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;
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
    private final DisasterReportGroupService groupService;

    // 제보 검토 - 전체 제보 목록 (제보 평가 및 신고수 포함)
    public AdminReportPageResponse findAllReports(List<Long> disasterTypeIds, List<RiskLevel> riskLevels, List<DisasterReportStatus> statuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 필터링 로직
        Page<DisasterReport> pageResult;
        boolean hasDisasterTypeFilter = disasterTypeIds != null && !disasterTypeIds.isEmpty();
        boolean hasRiskLevelFilter = riskLevels != null && !riskLevels.isEmpty();
        boolean hasStatusFilter = statuses != null && !statuses.isEmpty();

        if (!hasDisasterTypeFilter && !hasRiskLevelFilter && !hasStatusFilter) {
            pageResult = disasterReportRepository.findAll(pageable);
        } else if (hasDisasterTypeFilter && hasRiskLevelFilter && hasStatusFilter) {
            pageResult = disasterReportRepository.findByDisasterTypeIdInAndRiskLevelInAndStatusInOrderByCreatedAtDesc(disasterTypeIds, riskLevels, statuses, pageable);
        } else if (hasDisasterTypeFilter && hasRiskLevelFilter) {
            pageResult = disasterReportRepository.findByDisasterTypeIdInAndRiskLevelInOrderByCreatedAtDesc(disasterTypeIds, riskLevels, pageable);
        } else if (hasDisasterTypeFilter && hasStatusFilter) {
            pageResult = disasterReportRepository.findByDisasterTypeIdInAndStatusInOrderByCreatedAtDesc(disasterTypeIds, statuses, pageable);
        } else if (hasRiskLevelFilter && hasStatusFilter) {
            pageResult = disasterReportRepository.findByRiskLevelInAndStatusInOrderByCreatedAtDesc(riskLevels, statuses, pageable);
        } else if (hasDisasterTypeFilter) {
            pageResult = disasterReportRepository.findByDisasterTypeIdInOrderByCreatedAtDesc(disasterTypeIds, pageable);
        } else if (hasRiskLevelFilter) {
            pageResult = disasterReportRepository.findByRiskLevelInOrderByCreatedAtDesc(riskLevels, pageable);
        } else {
            pageResult = disasterReportRepository.findByStatusInOrderByCreatedAtDesc(statuses, pageable);
        }

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

    public DisasterReportResponse getById(Long reportId) {
        DisasterReport report = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(ErrorMessage.INVALID_DISASTER_REPORT));
        return DisasterReportResponse.of(report);
    }

    // 재난 제보 상태 변경 및 검토 의견 저장
    @Transactional
    public void updateStatus(Long reportId, DisasterReportStatusUpdateRequest request) {
        DisasterReport report = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(ErrorMessage.INVALID_DISASTER_REPORT));

        report.updateStatus(request.status(), request.reviewComment());

        if (request.status() == DisasterReportStatus.APPROVED) {
            // BLINDED였던 경우 group이 null이므로 새 제보처럼 그룹 재배치
            if (report.getGroup() == null) {
                groupService.reAssignReportToGroup(report);
            }
        } else if (request.status() == DisasterReportStatus.BLINDED) {
            // 그룹에서 제거 후 통계 재계산
            if (report.getGroup() != null) {
                Long groupId = report.getGroup().getId();
                report.getGroup().removeReport(report);
                groupService.calculateGroupStatistics(groupId);
            }
        }
    }
}
