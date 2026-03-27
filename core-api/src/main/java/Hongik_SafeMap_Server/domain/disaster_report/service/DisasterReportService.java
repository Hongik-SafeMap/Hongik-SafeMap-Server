package Hongik_SafeMap_Server.domain.disaster_report.service;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReportEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.domain.UserEvaluation;
import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportEvaluationResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportPageResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.disaster_report.repository.UserEvaluationRepository;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
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

import static Hongik_SafeMap_Server.exception.ErrorMessage.INVALID_DISASTER_REPORT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisasterReportService {
    private final DisasterReportRepository disasterReportRepository;
    private final DisasterReportEvaluationRepository evaluationRepository;
    private final UserEvaluationRepository userEvaluationRepository;
    private final DisasterReportGroupService groupService;
    private final MemberUtil memberUtil;

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
    public Page<DisasterReportListResponse> getMyReports(int page, int size) {
        Member member = memberUtil.getLoggedInMember();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return disasterReportRepository.findByMemberOrderByCreatedAtDesc(member, pageable)
                .map(DisasterReportListResponse::of);
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
        if (disasterReport.getStatus() == DisasterReportStatus.FALSE) {
            throw new DisasterReportException(ErrorMessage.CANNOT_APPROVE_FALSE_REPORT);
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

    // 관리자 제보 허위 처리
    @Transactional
    public void markFalse(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        Long groupId = null;
        // 그룹에서 제거 (허위 제보는 그룹 통계에서 제외)
        if (disasterReport.getGroup() != null) {
            groupId = disasterReport.getGroup().getId();
            disasterReport.getGroup().removeReport(disasterReport);
        }

        disasterReport.markFalse();

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

        // 기존 사용자 평가 조회 또는 새로 생성
        UserEvaluation userEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                        member.getId(), reportId)
                .orElse(new UserEvaluation(member, disasterReport));

        // 집계 카운트 조회
        DisasterReportEvaluation evaluation = evaluationRepository.findById(reportId)
                .orElseGet(() -> new DisasterReportEvaluation(disasterReport));

        // 이미 평가했는지 확인
        boolean alreadyEvaluated = getPreviousEvaluationValue(userEvaluation, evaluationType);

        if (!alreadyEvaluated) {
            // 평가 추가
            updateUserEvaluation(userEvaluation, evaluationType, true);
            userEvaluationRepository.save(userEvaluation);

            // 집계 카운트 증가
            evaluation.increase(evaluationType);
            evaluationRepository.save(evaluation);
        }
    }

    // 제보 평가 취소하기
    @Transactional
    public void deleteEvaluation(Long reportId, DisasterReportEvaluationType evaluationType) {
        Member member = memberUtil.getLoggedInMember();
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        // 기존 사용자 평가 조회
        UserEvaluation userEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                        member.getId(), reportId)
                .orElse(null);

        if (userEvaluation != null) {
            boolean previousValue = getPreviousEvaluationValue(userEvaluation, evaluationType);

            if (previousValue) {
                // 평가를 false로 변경
                updateUserEvaluation(userEvaluation, evaluationType, false);
                userEvaluationRepository.save(userEvaluation);

                // 집계 카운트 감소
                DisasterReportEvaluation evaluation = evaluationRepository.findById(reportId)
                        .orElseGet(() -> new DisasterReportEvaluation(disasterReport));
                evaluation.decrease(evaluationType);
                evaluationRepository.save(evaluation);
            }
        }
    }

    // 제보 평가 조회
    @Transactional(readOnly = true)
    public DisasterReportEvaluationResponse getReportEvaluation(Long reportId) {
        Member member = memberUtil.getLoggedInMember();
        disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        DisasterReportEvaluation evaluation = evaluationRepository.findDisasterReportEvaluationById(reportId);

        // 사용자의 평가 정보 조회
        UserEvaluation userEvaluation = userEvaluationRepository.findByMemberIdAndDisasterReportId(
                member.getId(), reportId).orElse(null);

        boolean userEvaluatedHelpful = userEvaluation != null && userEvaluation.getIsHelpful();
        boolean userEvaluatedNotHelpful = userEvaluation != null && userEvaluation.getIsNotHelpful();
        boolean userEvaluatedAccurate = userEvaluation != null && userEvaluation.getIsAccurate();
        boolean userEvaluatedFalseReport = userEvaluation != null && userEvaluation.getIsFalseReport();

        return DisasterReportEvaluationResponse.of(evaluation,
                userEvaluatedHelpful, userEvaluatedNotHelpful,
                userEvaluatedAccurate, userEvaluatedFalseReport);
    }

    private boolean getPreviousEvaluationValue(UserEvaluation userEvaluation, DisasterReportEvaluationType evaluationType) {
        return switch (evaluationType) {
            case HELPFUL -> userEvaluation.getIsHelpful();
            case NOT_HELPFUL -> userEvaluation.getIsNotHelpful();
            case ACCURATE -> userEvaluation.getIsAccurate();
            case FALSE_REPORT -> userEvaluation.getIsFalseReport();
        };
    }

    private void updateUserEvaluation(UserEvaluation userEvaluation, DisasterReportEvaluationType evaluationType, boolean value) {
        switch (evaluationType) {
            case HELPFUL -> userEvaluation.updateEvaluations(value, null, null, null);
            case NOT_HELPFUL -> userEvaluation.updateEvaluations(null, value, null, null);
            case ACCURATE -> userEvaluation.updateEvaluations(null, null, value, null);
            case FALSE_REPORT -> userEvaluation.updateEvaluations(null, null, null, value);
        }
    }


}
