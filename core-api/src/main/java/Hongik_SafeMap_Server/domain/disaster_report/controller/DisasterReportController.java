package Hongik_SafeMap_Server.domain.disaster_report.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
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
    public ResponseEntity<DisasterReportResponse> getById(@PathVariable Long reportId) {
        return ResponseEntity.ok(disasterReportService.getById(reportId));
    }

    // 전체 제보 목록 조회 (지도/관리자 제보검토용)
    @GetMapping
    public ResponseEntity<DisasterReportPageResponse> getAll(
            @RequestParam(required = false) List<DisasterType> disasterTypes,
            @RequestParam(required = false) List<RiskLevel> riskLevels,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(disasterReportService.getAll(disasterTypes, riskLevels, page, size));
    }
}
