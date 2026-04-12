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
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.global.service.NotificationService;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.DisasterReportEvaluationType;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisasterReportService {
    private final DisasterReportRepository disasterReportRepository;
    private final DisasterReportEvaluationRepository evaluationRepository;
    private final DisasterReportAccusationRepository accusationRepository;
    private final UserEvaluationRepository userEvaluationRepository;
    private final DisasterReportGroupService groupService;
    private final MemberUtil memberUtil;
    private final NotificationService notificationService;

    // 긴급 제보 등록
    @Transactional
    public Long create(DisasterReportCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        DisasterReport disasterReport = DisasterReport.builder()
                .disasterType(request.disasterType())
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

        // 그룹에 할당 (@TODO: 비동기 처리)
        groupService.assignReportToGroup(savedReport);

        // 해당 재난 유형에 대해 알림을 활성화한 사용자들에게 알림 전송
        notificationService.sendDisasterReportNotification(
                savedReport.getDisasterType(),
                savedReport.getAddress()
        );

        return savedReport.getId();
    }

    // 제보 조회 (일반/관리자 공용)
    public DisasterReportResponse getById(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        return DisasterReportResponse.of(disasterReport);
    }

    // 전체 제보 목록 (관리자 전체 제보/지도)
    public DisasterReportPageResponse getAll(List<DisasterType> disasterTypes, List<RiskLevel> riskLevels, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<DisasterReportListResponse> pageResult;
        if ((disasterTypes == null || disasterTypes.isEmpty()) && (riskLevels == null || riskLevels.isEmpty())) {
            // 필터 없음 - 전체 조회
            pageResult = disasterReportRepository.findAllByOrderByCreatedAtDesc(pageable)
                    .map(DisasterReportListResponse::of);
        } else if (disasterTypes != null && !disasterTypes.isEmpty() && riskLevels != null && !riskLevels.isEmpty()) {
            // 재난 유형과 긴급도 둘 다 필터링
            pageResult = disasterReportRepository.findByDisasterTypeInAndRiskLevelInOrderByCreatedAtDesc(disasterTypes, riskLevels, pageable)
                    .map(DisasterReportListResponse::of);
        } else if (disasterTypes != null && !disasterTypes.isEmpty()) {
            // 재난 유형만 필터링
            pageResult = disasterReportRepository.findByDisasterTypeInOrderByCreatedAtDesc(disasterTypes, pageable)
                    .map(DisasterReportListResponse::of);
        } else {
            // 긴급도만 필터링
            pageResult = disasterReportRepository.findByRiskLevelInOrderByCreatedAtDesc(riskLevels, pageable)
                    .map(DisasterReportListResponse::of);
        }

        List<DisasterReportListResponse> reports = pageResult.getContent();

        return new DisasterReportPageResponse(
                reports,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
    }

    // 내 제보 목록(마이 페이지)
    public DisasterReportPageResponse getMyReports(int page, int size) {
        Member member = memberUtil.getLoggedInMember();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<DisasterReport> reportPage = disasterReportRepository.findByMemberOrderByCreatedAtDesc(member, pageable);

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

    // 관리자 제보 승인 처리
    @Transactional
    public void approve(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        // 이미 처리된 제보인지 검증(@TODO: 변경 가능하게 수정 - 통계 업데이트 문제 때문에 임시 에러 처리)
        if (disasterReport.getStatus() == DisasterReportStatus.BLINDED) {
            throw new DisasterReportException(ErrorMessage.CANNOT_APPROVE_BLINDED_REPORT);
        }
        disasterReport.approve();
    }

    // 관리자 제보 블라인드 처리
    @Transactional
    public void blind(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        Long groupId = null;
        // 그룹에서 제거 (블라인드된 제보는 그룹 통계에서 제외)
        if (disasterReport.getGroup() != null) {
            groupId = disasterReport.getGroup().getId();
            disasterReport.getGroup().removeReport(disasterReport);
        }

        disasterReport.blind();

        // 그룹 통계 재계산
        if (groupId != null) {
            groupService.calculateGroupStatistics(groupId);
        }
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


    // 도움 안됨 + 도움 됨 + 신고 count
    public DisasterReportStatisticsResponse getStatistics(Long disasterReportId) {
        if (!disasterReportRepository.existsById(disasterReportId)) {
            throw new DisasterReportException(INVALID_DISASTER_REPORT);
        }

        DisasterReportEvaluation evaluation = evaluationRepository.findDisasterReportEvaluationById(disasterReportId).orElse(null);
        int accusationCount = accusationRepository.countByDisasterReportId(disasterReportId);

        return DisasterReportStatisticsResponse.of(disasterReportId, evaluation, accusationCount);
    }
}
