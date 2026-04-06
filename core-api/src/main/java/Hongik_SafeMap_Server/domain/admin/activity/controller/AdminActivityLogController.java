package Hongik_SafeMap_Server.domain.admin.activity.controller;

import Hongik_SafeMap_Server.domain.admin.activity.dto.response.AdminActivityLogResponse;
import Hongik_SafeMap_Server.domain.admin.activity.service.AdminActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 활동 로그")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/activity-logs")
public class AdminActivityLogController {

    private final AdminActivityLogService adminActivityLogService;

    @Operation(summary = "관리자 활동 로그 조회", description = "모든 관리자의 활동 로그를 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminActivityLogResponse>> getActivityLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminActivityLogResponse> logs = adminActivityLogService.getActivityLogs(page, size);
        return ResponseEntity.ok().body(logs);
    }
}