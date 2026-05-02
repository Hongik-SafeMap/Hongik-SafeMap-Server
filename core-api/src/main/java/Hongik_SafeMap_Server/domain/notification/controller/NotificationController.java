package Hongik_SafeMap_Server.domain.notification.controller;

import Hongik_SafeMap_Server.domain.notification.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPageResponse;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPreferenceResponse;
import Hongik_SafeMap_Server.domain.notification.service.NotificationPreferenceService;
import Hongik_SafeMap_Server.domain.notification.service.NotificationQueryService;
import Hongik_SafeMap_Server.util.MemberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림", description = "알림 관련 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final MemberUtil memberUtil;
    private final NotificationPreferenceService notificationPreferenceService;

    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<NotificationPageResponse> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        val member = memberUtil.getLoggedInMember();
        val notifications = notificationQueryService.getNotifications(member.getId(), page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "사용자의 읽지 않은 알림 개수를 조회합니다.")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        val member = memberUtil.getLoggedInMember();
        val count = notificationQueryService.getUnreadCount(member.getId());
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "알림 읽음 처리", description = "모든 알림을 읽음으로 표시합니다.")
    @PatchMapping("/read")
    public ResponseEntity<Void> markAsRead() {
        val member = memberUtil.getLoggedInMember();
        notificationQueryService.markAsRead(member.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 설정 조회", description = "사용자의 재난 유형별 알림 설정을 조회합니다")
    @GetMapping("/preferences")
    public ResponseEntity<List<NotificationPreferenceResponse>> getNotificationPreferences() {
        val preferences = notificationPreferenceService.getNotificationPreferences();
        return ResponseEntity.ok(preferences);
    }

    @Operation(summary = "알림 설정 변경", description = "특정 재난 유형의 알림 설정을 변경합니다")
    @PutMapping("/preferences")
    public ResponseEntity<String> updateNotificationPreference(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        notificationPreferenceService.updateNotificationPreference(request);
        return ResponseEntity.ok("알림 설정이 변경되었습니다");
    }
}
