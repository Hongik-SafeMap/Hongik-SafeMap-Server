package Hongik_SafeMap_Server.domain.disaster_report.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportEvaluationRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportEvaluationResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportPageResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disaster-reports")
public class DisasterReportController {
    private final DisasterReportService disasterReportService;

    // 긴급 제보 등록
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody DisasterReportCreateRequest createRequest) {
        Long reportId = disasterReportService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    // 제보 조회(일반, 관리자 공용)
    @GetMapping("/{reportId}")
    public ResponseEntity<DisasterReportResponse> getById(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(disasterReportService.getById(reportId));
    }

    // 전체 제보 목록 조회 (관리자 제보검토용)
    @GetMapping
    public ResponseEntity<DisasterReportPageResponse> getAll(
            @RequestParam(value = "disasterTypes", required = false) List<DisasterType> disasterTypes,
            @RequestParam(value = "riskLevels", required = false) List<RiskLevel> riskLevels,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(disasterReportService.getAll(disasterTypes, riskLevels, page, size));
    }

    // 제보 평가하기
    @PostMapping("/{reportId}/evaluations")
    public ResponseEntity<Void> createReportEvaluation(
            @PathVariable("reportId") Long reportId,
            @Valid @RequestBody DisasterReportEvaluationRequest request) {
        disasterReportService.evaluateReport(reportId, request.evaluationType());
        return ResponseEntity.noContent().build();
    }

    // 제보 평가 취소하기
    @DeleteMapping("/{reportId}/evaluations")
    public ResponseEntity<Void> deleteReportEvaluation(
            @PathVariable("reportId") Long reportId,
            @Valid @RequestBody DisasterReportEvaluationRequest request) {
        disasterReportService.deleteEvaluation(reportId, request.evaluationType());
        return ResponseEntity.noContent().build();
    }

    // 제보 평가 조회
    @GetMapping("/{reportId}/evaluations")
    public ResponseEntity<DisasterReportEvaluationResponse> getReportEvaluation(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(disasterReportService.getReportEvaluation(reportId));
    }
}
