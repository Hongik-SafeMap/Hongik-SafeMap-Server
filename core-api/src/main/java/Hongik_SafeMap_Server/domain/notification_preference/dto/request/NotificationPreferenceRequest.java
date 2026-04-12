package Hongik_SafeMap_Server.domain.notification_preference.dto.request;

import Hongik_SafeMap_Server.vo.DisasterType;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record NotificationPreferenceRequest(
        DisasterType disasterType,
        boolean isEnabled
) {

    public static NotificationPreferenceRequest of(DisasterType disasterType, boolean isEnabled) {
        return NotificationPreferenceRequest.builder()
                .disasterType(disasterType)
                .isEnabled(isEnabled)
                .build();
    }
}