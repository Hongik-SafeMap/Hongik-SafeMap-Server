package Hongik_SafeMap_Server.domain.notification_preference.controller;

import Hongik_SafeMap_Server.domain.notification_preference.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification_preference.dto.response.NotificationPreferenceResponse;
import Hongik_SafeMap_Server.domain.notification_preference.service.NotificationPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 설정", description = "재난 유형별 알림 설정 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    @Operation(summary = "알림 설정 조회", description = "사용자의 재난 유형별 알림 설정을 조회합니다")
    @GetMapping
    public ResponseEntity<List<NotificationPreferenceResponse>> getNotificationPreferences() {
        val preferences = notificationPreferenceService.getNotificationPreferences();
        return ResponseEntity.ok(preferences);
    }

    @Operation(summary = "알림 설정 변경", description = "특정 재난 유형의 알림 설정을 변경합니다")
    @PutMapping
    public ResponseEntity<String> updateNotificationPreference(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        notificationPreferenceService.updateNotificationPreference(request);
        return ResponseEntity.ok("알림 설정이 변경되었습니다");
    }
}