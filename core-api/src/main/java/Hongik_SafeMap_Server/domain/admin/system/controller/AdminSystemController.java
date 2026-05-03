package Hongik_SafeMap_Server.domain.admin.system.controller;

import Hongik_SafeMap_Server.domain.admin.system.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시스템 운영 모드", description = "서버 점검 등 시스템 운영 관련 API")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/system")
public class AdminSystemController {

    private final MaintenanceService maintenanceService;

    @Operation(summary = "서버 점검 상태 조회")
    @GetMapping("/maintenance")
    public ResponseEntity<MaintenanceStatusResponse> getMaintenanceStatus() {
        return ResponseEntity.ok(new MaintenanceStatusResponse(maintenanceService.isUnderMaintenance()));
    }

    @Operation(summary = "서버 점검 상태 변경", description = "status가 true이면 점검 모드 활성화, false이면 해제합니다.")
    @PatchMapping("/maintenance")
    public ResponseEntity<Void> setMaintenance(@RequestParam(name = "status") boolean status) {
        maintenanceService.setMaintenance(status);
        return ResponseEntity.ok().build();
    }

    record MaintenanceStatusResponse(boolean maintenance) {
    }
}
