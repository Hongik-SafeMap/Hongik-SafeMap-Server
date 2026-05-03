package Hongik_SafeMap_Server.domain.disaster_report.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportEvaluationRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportEvaluationResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disaster-reports")
@Tag(name = "재난 제보", description = "재난 제보 관련 API")
public class DisasterReportController {
    private final DisasterReportService disasterReportService;

    @Operation(summary = "재난 상황 제보", description = "재난 상황을 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody DisasterReportCreateRequest createRequest) {
        Long reportId = disasterReportService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    @Operation(summary = "재난 상황 상세 조회", description = "재난 상황을 상세 조회합니다.")
    @GetMapping("/{reportId}")
    public ResponseEntity<DisasterReportResponse> getById(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(disasterReportService.getById(reportId));
    }


    @Operation(summary = "재난 제보 평가", description = "ID로 제보를 평가합니다. 여러 종류의 평가를 남길 수 있습니다.")
    @PostMapping("/{reportId}/evaluations")
    public ResponseEntity<Void> createReportEvaluation(
            @PathVariable("reportId") Long reportId,
            @Valid @RequestBody DisasterReportEvaluationRequest request) {
        disasterReportService.evaluateReport(reportId, request.evaluationType());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "재난 제보 평가 취소", description = "ID로 제보 평가를 취소합니다.")
    @DeleteMapping("/{reportId}/evaluations")
    public ResponseEntity<Void> deleteReportEvaluation(@PathVariable("reportId") Long reportId) {
        disasterReportService.deleteEvaluation(reportId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "재난 제보 평가 조회", description = "ID로 제보를 평가를 조회합니다.")
    @GetMapping("/{reportId}/evaluations")
    public ResponseEntity<DisasterReportEvaluationResponse> getReportEvaluation(@PathVariable("reportId") Long reportId) {
        return ResponseEntity.ok(disasterReportService.getReportEvaluation(reportId));
    }

    @Operation(summary = "재난 제보 신고", description = "재난 제보를 신고합니다.")
    @PostMapping("/{reportId}/accusation")
    public ResponseEntity<Void> accuseReport(@PathVariable("reportId") Long reportId) {
        disasterReportService.accuseReport(reportId);
        return ResponseEntity.ok().build();
    }
}
