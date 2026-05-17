package Hongik_SafeMap_Server.domain.admin.notification;

import Hongik_SafeMap_Server.domain.notification.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPreferenceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 알림", description = "관리자 알림 설정 API")
@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationPreferenceService notificationPreferenceService;

    @Operation(summary = "알림 설정 조회", description = "관리자용 재난 유형별 알림 설정을 조회합니다")
    @GetMapping("/preferences")
    public ResponseEntity<List<NotificationPreferenceResponse>> getNotificationPreferences() {
        return ResponseEntity.ok(notificationPreferenceService.getNotificationPreferences());
    }

    @Operation(summary = "알림 설정 변경", description = "특정 재난 유형의 알림 설정을 변경합니다")
    @PutMapping("/preferences")
    public ResponseEntity<String> updateNotificationPreference(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        notificationPreferenceService.updateNotificationPreference(request);
        return ResponseEntity.ok("알림 설정이 변경되었습니다");
    }
}
