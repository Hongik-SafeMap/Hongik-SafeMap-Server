package Hongik_SafeMap_Server.domain.notification.dto.request;

public record NotificationPreferenceRequest(
        Long disasterTypeId,
        boolean isEnabled
) {
}