package Hongik_SafeMap_Server.domain.notification.service;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
import Hongik_SafeMap_Server.domain.notification.domain.NotificationPreference;
import Hongik_SafeMap_Server.domain.notification.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPreferenceResponse;
import Hongik_SafeMap_Server.domain.notification.repository.NotificationPreferenceRepository;
import Hongik_SafeMap_Server.exception.DisasterTypeException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Hongik_SafeMap_Server.exception.ErrorMessage.DISASTER_TYPE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final DisasterTypeRepository disasterTypeRepository;
    private final MemberUtil memberUtil;

    public List<NotificationPreferenceResponse> getNotificationPreferences() {
        val member = memberUtil.getLoggedInMember();
        val preferences = notificationPreferenceRepository.findByMemberId(member.getId());

        Map<Long, NotificationPreference> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(
                        p -> p.getDisasterType().getId(),
                        p -> p
                ));

        return disasterTypeRepository.findAllByOrderByIdAsc().stream()
                .map(disasterType -> {
                    NotificationPreference preference = preferenceMap.get(disasterType.getId());
                    return preference != null
                            ? NotificationPreferenceResponse.of(preference)
                            : NotificationPreferenceResponse.of(disasterType, false);
                })
                .toList();
    }

    @Transactional
    public void updateNotificationPreference(NotificationPreferenceRequest request) {
        val member = memberUtil.getLoggedInMember();
        val disasterType = disasterTypeRepository.findById(request.disasterTypeId())
                .orElseThrow(() -> new DisasterTypeException(DISASTER_TYPE_NOT_FOUND));

        val existingPreference = notificationPreferenceRepository
                .findByMemberIdAndDisasterType(member.getId(), disasterType);

        if (existingPreference.isPresent()) {
            existingPreference.get().updateEnabled(request.isEnabled());
        } else {
            val newPreference = NotificationPreference.builder()
                    .member(member)
                    .disasterType(disasterType)
                    .isEnabled(request.isEnabled())
                    .build();
            notificationPreferenceRepository.save(newPreference);
        }
    }
}