package Hongik_SafeMap_Server.domain.admin.disaster_review.controller;

import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.request.DisasterReportStatusUpdateRequest;
import Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response.AdminReportPageResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_review.service.AdminReportService;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.RiskLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 제보 검토")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/disaster-reports")
public class AdminReportController {
    private final AdminReportService adminReportService;
    private final DisasterReportService disasterReportService;

    @Operation(summary = "재난 상황 목록 조회", description = "재난 상황 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<AdminReportPageResponse> getAll(
            @RequestParam(value = "disasterTypeIds", required = false) List<Long> disasterTypeIds,
            @RequestParam(value = "riskLevels", required = false) List<RiskLevel> riskLevels,
            @RequestParam(value = "statuses", required = false) List<DisasterReportStatus> statuses,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(adminReportService.findAllReports(disasterTypeIds, riskLevels, statuses, page, size));
    }

    @Operation(summary = "재난 제보 상태 변경", description = "재난 제보의 상태를 변경하고 검토 의견을 남깁니다.")
    @PutMapping("/{reportId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable("reportId") Long reportId,
            @Valid @RequestBody DisasterReportStatusUpdateRequest request) {
        adminReportService.updateStatus(reportId, request);
        return ResponseEntity.ok().build();
    }
}
