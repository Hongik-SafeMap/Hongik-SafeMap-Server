package Hongik_SafeMap_Server.domain.disaster_report.service;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportPageResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.util.MemberUtil;
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

        return disasterReportRepository.save(disasterReport).getId();
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

        disasterReport.approve();
    }

    // 관리자 제보 블라인드 처리
    @Transactional
    public void blind(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        disasterReport.blind();
    }

    // 관리자 제보 허위 처리
    @Transactional
    public void markFalse(Long reportId) {
        DisasterReport disasterReport = disasterReportRepository.findById(reportId)
                .orElseThrow(() -> new DisasterReportException(INVALID_DISASTER_REPORT));

        disasterReport.markFalse();
    }
}
