package Hongik_SafeMap_Server.domain.disaster_report.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.request.DisasterReportCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<DisasterReportListResponse>> getAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(disasterReportService.getAll(pageable));
    }
}
