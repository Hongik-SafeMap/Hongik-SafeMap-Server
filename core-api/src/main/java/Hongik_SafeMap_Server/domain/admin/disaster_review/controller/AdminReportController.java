package Hongik_SafeMap_Server.domain.admin.disaster_review.controller;

import Hongik_SafeMap_Server.domain.admin.disaster_review.service.AdminReportService;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportPageResponse;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "제보 검토")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/disaster-reports")
public class AdminReportController {
    private final AdminReportService adminReportService;
    private final DisasterReportService disasterReportService;

    @Operation(summary = "재난 상황 목록 조회", description = "재난 상황 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<DisasterReportPageResponse> getAll(
            @RequestParam(value = "disasterTypes", required = false) List<DisasterType> disasterTypes,
            @RequestParam(value = "riskLevels", required = false) List<RiskLevel> riskLevels,
            @RequestParam(value = "statuses", required = false) List<DisasterReportStatus> statuses,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(disasterReportService.getAll(disasterTypes, riskLevels, statuses, page, size));
    }
}
