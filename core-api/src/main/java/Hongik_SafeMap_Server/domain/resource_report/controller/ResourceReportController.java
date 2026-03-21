package Hongik_SafeMap_Server.domain.resource_report.controller;

import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.service.ResourceReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자원 요청/공급", description = "자원 요청/공급 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-board")
public class ResourceReportController {

    private final ResourceReportService resourceReportService;

    @Operation(summary = "자원 요청/공급 등록", description = "자원 요청 또는 공급 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ResourceReportCreateRequest request) {
        Long reportId = resourceReportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }
}