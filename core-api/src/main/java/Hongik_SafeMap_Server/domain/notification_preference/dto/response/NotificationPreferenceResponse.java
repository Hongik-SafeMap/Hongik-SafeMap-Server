package Hongik_SafeMap_Server.domain.notification_preference.dto.response;

import Hongik_SafeMap_Server.domain.notification_preference.domain.NotificationPreference;
import Hongik_SafeMap_Server.vo.DisasterType;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record NotificationPreferenceResponse(
        DisasterType disasterType,
        boolean isEnabled
) {

    public static NotificationPreferenceResponse of(NotificationPreference notificationPreference) {
        return NotificationPreferenceResponse.builder()
                .disasterType(notificationPreference.getDisasterType())
                .isEnabled(notificationPreference.isEnabled())
                .build();
    }

    public static NotificationPreferenceResponse of(DisasterType disasterType, boolean isEnabled) {
        return NotificationPreferenceResponse.builder()
                .disasterType(disasterType)
                .isEnabled(isEnabled)
                .build();
    }
}