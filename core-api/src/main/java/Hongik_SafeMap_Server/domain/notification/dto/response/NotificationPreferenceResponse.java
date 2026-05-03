package Hongik_SafeMap_Server.domain.notification.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.notification.domain.NotificationPreference;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record NotificationPreferenceResponse(
        Long disasterTypeId,
        String disasterTypeName,
        String iconUrl,
        boolean isEnabled
) {

    public static NotificationPreferenceResponse of(NotificationPreference notificationPreference) {
        DisasterType dt = notificationPreference.getDisasterType();
        return NotificationPreferenceResponse.builder()
                .disasterTypeId(dt.getId())
                .disasterTypeName(dt.getName())
                .iconUrl(dt.getIconUrl())
                .isEnabled(notificationPreference.isEnabled())
                .build();
    }

    public static NotificationPreferenceResponse of(DisasterType disasterType, boolean isEnabled) {
        return NotificationPreferenceResponse.builder()
                .disasterTypeId(disasterType.getId())
                .disasterTypeName(disasterType.getName())
                .iconUrl(disasterType.getIconUrl())
                .isEnabled(isEnabled)
                .build();
    }
}