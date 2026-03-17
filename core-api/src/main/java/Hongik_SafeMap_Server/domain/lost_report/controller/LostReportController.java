package Hongik_SafeMap_Server.domain.lost_report.controller;

import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportResponse;
import Hongik_SafeMap_Server.domain.lost_report.service.LostReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "실종 신고", description = "실종 신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/missing-board")
public class LostReportController {

    private final LostReportService lostReportService;

    @Operation(summary = "실종 신고 등록", description = "실종자 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody LostReportCreateRequest request) {
        Long reportId = lostReportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    @Operation(summary = "실종 신고 상세 조회", description = "실종신고 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<LostReportResponse> getById(@PathVariable Long id) {
        LostReportResponse response = lostReportService.getById(id);
        return ResponseEntity.ok(response);
    }
}