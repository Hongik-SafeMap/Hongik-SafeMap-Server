package Hongik_SafeMap_Server.domain.admin.notification;

import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
import Hongik_SafeMap_Server.domain.notification.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPreferenceResponse;
import Hongik_SafeMap_Server.exception.DisasterTypeException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.DISASTER_TYPE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminNotificationPreferenceService {

    private final DisasterTypeRepository disasterTypeRepository;

    public List<NotificationPreferenceResponse> getNotificationPreferences() {
        return disasterTypeRepository.findAllByOrderByIdAsc().stream()
                .map(dt -> NotificationPreferenceResponse.of(dt, dt.isNotificationEnabled()))
                .toList();
    }

    @Transactional
    public void updateNotificationPreference(NotificationPreferenceRequest request) {
        val disasterType = disasterTypeRepository.findById(request.disasterTypeId())
                .orElseThrow(() -> new DisasterTypeException(DISASTER_TYPE_NOT_FOUND));
        disasterType.updateNotificationEnabled(request.isEnabled());
    }
}
