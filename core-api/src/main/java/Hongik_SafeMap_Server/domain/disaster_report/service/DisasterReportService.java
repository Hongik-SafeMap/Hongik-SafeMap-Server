package Hongik_SafeMap_Server.domain.disaster_report.service;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportAccusation;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.domain.UserEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.*;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportAccusationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.UserEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.service.DisasterTypeService;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.global.service.NotificationService;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.DisasterReportEvaluationType;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisasterReportService {
    private final DisasterReportRepository disasterReportRepository;
    private final DisasterReportEvaluationRepository evaluationRepository;
    private final DisasterReportAccusationRepository accusationRepository;
    private final UserEvaluationRepository userEvaluationRepository;
    private final DisasterReportGroupService groupService;
    private final DisasterTypeService disasterTypeService;
    private final MemberUtil memberUtil;
    private final AiAnalyzeClient aiAnalyzeClient;
    private final NotificationService notificationService;

    // 긴급 제보 등록
    @Transactional
    public Long create(DisasterReportCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();
        DisasterType disasterType = disasterTypeService.getEntityById(request.disasterTypeId());

        DisasterReport disasterReport = DisasterReport.builder()
                .disasterType(disasterType)
                .riskLevel(request.riskLevel())
                .disasterDescription(request.disasterDescription())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .address(request.address())
                .fileUrls(request.fileUrls())
                .status(DisasterReportStatus.PENDING)
                .member(member)
                .build();

        // 제보 저장
        DisasterReport savedReport = disasterReportRepository.save(disasterReport);

        analyzeReportImage(savedReport);

        // 그룹에 할당 (@TODO: 비동기 처리)
        groupService.assignReportToGroup(savedReport);

        // 해당 재난 유형에 대해 알림을 활성화한 사용자들에게 알림 전송
        notificationService.sendDisasterReportNotification(
                savedReport.getDisasterType(),
                savedReport.getAddress()
        );

        return savedReport.getId();
    }

    // 제보 상세 조회
    public DisasterReportResponse getById(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        if (disasterReport.getStatus() == DisasterReportStatus.BLINDED) {
            throw new DisasterReportException(ErrorMessage.BLINDED_DISASTER_REPORT);
        }

        return DisasterReportResponse.of(disasterReport);
    }

    // 내 제보 목록(마이 페이지)
    public DisasterReportPageResponse getMyReports(int page, int size) {
        Member member = memberUtil.getLoggedInMember();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<DisasterReport> reportPage = disasterReportRepository.findByMemberAndStatusNotOrderByCreatedAtDesc(member, DisasterReportStatus.BLINDED, pageable);

        List<DisasterReportListResponse> reports = reportPage.getContent().stream()
                .map(DisasterReportListResponse::of)
                .toList();

        return new DisasterReportPageResponse(
                reports,
                reportPage.getNumber(),
                reportPage.getSize(),
                reportPage.getTotalElements(),
                reportPage.getTotalPages(),
                reportPage.isFirst(),
                reportPage.isLast()
        );
    }

    // 제보 평가하기
    @Transactional
    public void evaluateReport(Long reportId, DisasterReportEvaluationType evaluationType) {
        Member member = memberUtil.getLoggedInMember();
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        UserEvaluation existingUserEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                member.getId(), reportId).orElse(null);

        // 이미 해당 타입으로 평가함
        if (existingUserEvaluation != null && existingUserEvaluation.hasEvaluationType(evaluationType)) {
            return;
        }

        DisasterReportEvaluation evaluation = evaluationRepository.findById(reportId)
                .orElseGet(() -> new DisasterReportEvaluation(disasterReport));

        // 이전에 다른 평가를 했다면 해당 평가 카운트 감소
        if (existingUserEvaluation != null && existingUserEvaluation.hasEvaluation()) {
            DisasterReportEvaluationType previousType = existingUserEvaluation.getEvaluationType();
            evaluation.decrease(previousType);
        }

        // 사용자 평가 생성/업데이트
        UserEvaluation userEvaluation;
        if (existingUserEvaluation == null) {
            userEvaluation = new UserEvaluation(member, disasterReport, evaluationType);
        } else {
            existingUserEvaluation.updateEvaluation(evaluationType);
            userEvaluation = existingUserEvaluation;
        }

        // 새 평가 카운트 증가
        evaluation.increase(evaluationType);

        // 저장
        userEvaluationRepository.save(userEvaluation);
        evaluationRepository.save(evaluation);
    }

    // 제보 평가 취소하기
    @Transactional
    public void deleteEvaluation(Long reportId) {
        Member member = memberUtil.getLoggedInMember();
        disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        UserEvaluation userEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                        member.getId(), reportId)
                .orElse(null);

        if (userEvaluation != null) {
            DisasterReportEvaluation evaluation = evaluationRepository.findById(reportId)
                    .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT_EVALUATION));
            evaluation.decrease(userEvaluation.getEvaluationType());
            evaluationRepository.save(evaluation);
            userEvaluationRepository.delete(userEvaluation);
        }
    }

    // 제보 평가 조회
    @Transactional(readOnly = true)
    public DisasterReportEvaluationResponse getReportEvaluation(Long reportId) {
        Member member = memberUtil.getLoggedInMember();
        disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        DisasterReportEvaluation evaluation = evaluationRepository.findDisasterReportEvaluationById(reportId).orElse(null);
        if (evaluation == null) {
            return DisasterReportEvaluationResponse.ofDefault();
        }

        // 사용자의 평가 정보 조회
        UserEvaluation userEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                member.getId(), reportId).orElse(null);

        boolean userEvaluatedHelpful = userEvaluation != null && userEvaluation.hasEvaluationType(DisasterReportEvaluationType.HELPFUL);
        boolean userEvaluatedNotHelpful = userEvaluation != null && userEvaluation.hasEvaluationType(DisasterReportEvaluationType.NOT_HELPFUL);

        return DisasterReportEvaluationResponse.of(evaluation,
                userEvaluatedHelpful, userEvaluatedNotHelpful);
    }

    @Transactional
    public void accuseReport(Long disasterReportId) {
        Member member = memberUtil.getLoggedInMember();

        DisasterReport disasterReport = disasterReportRepository.findById(disasterReportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        if (accusationRepository.existsByMemberIdAndDisasterReportId(member.getId(), disasterReportId)) {
            throw new DisasterReportException(ALREADY_ACCUSED_DISASTER_REPORT);
        }

        DisasterReportAccusation accusation = DisasterReportAccusation.builder()
                .member(member)
                .disasterReport(disasterReport)
                .build();

        accusationRepository.save(accusation);
    }

    private void analyzeReportImage(DisasterReport savedReport) {
        if (savedReport.getFileUrls() == null || savedReport.getFileUrls().isEmpty()) {
            return;
        }

        try {
            String imageUrl = savedReport.getFileUrls().get(0);

            AiAnalyzeResponse aiResult = aiAnalyzeClient.analyze(savedReport.getId(), imageUrl);

            savedReport.updateAiAnalysisResult(
                    aiResult.aiGeneratedProbability(),
                    aiResult.realProbability(),
                    aiResult.aiPrediction(),
                    aiResult.informativeProbability(),
                    aiResult.notInformativeProbability(),
                    aiResult.informativePrediction(),
                    aiResult.trustScore(),
                    DisasterReportStatus.valueOf(aiResult.status())
            );
        } catch (Exception e) {
            log.warn("AI 분석 실패 reportId={}, reason={}", savedReport.getId(), e.getMessage());
        }
    }
}
