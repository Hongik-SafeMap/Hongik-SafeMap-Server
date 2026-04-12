package Hongik_SafeMap_Server.domain.notification_preference.service;

import Hongik_SafeMap_Server.domain.notification_preference.domain.NotificationPreference;
import Hongik_SafeMap_Server.domain.notification_preference.dto.request.NotificationPreferenceRequest;
import Hongik_SafeMap_Server.domain.notification_preference.dto.response.NotificationPreferenceResponse;
import Hongik_SafeMap_Server.domain.notification_preference.repository.NotificationPreferenceRepository;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.DisasterType;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final MemberUtil memberUtil;

    public List<NotificationPreferenceResponse> getNotificationPreferences() {
        val member = memberUtil.getLoggedInMember();
        val preferences = notificationPreferenceRepository.findByMemberId(member.getId());

        // 기존 설정을 Map으로 변환
        Map<DisasterType, NotificationPreference> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(
                        NotificationPreference::getDisasterType,
                        preference -> preference
                ));

        // 모든 DisasterType에 대해 응답 생성 (기본값: false)
        return Arrays.stream(DisasterType.values())
                .map(disasterType -> {
                    NotificationPreference preference = preferenceMap.get(disasterType);
                    return preference != null
                            ? NotificationPreferenceResponse.of(preference)
                            : NotificationPreferenceResponse.of(disasterType, false);
                })
                .toList();
    }

    @Transactional
    public void updateNotificationPreference(NotificationPreferenceRequest request) {
        val member = memberUtil.getLoggedInMember();

        val existingPreference = notificationPreferenceRepository
                .findByMemberIdAndDisasterType(member.getId(), request.disasterType());

        if (existingPreference.isPresent()) {
            existingPreference.get().updateEnabled(request.isEnabled());
        } else {
            val newPreference = NotificationPreference.builder()
                    .member(member)
                    .disasterType(request.disasterType())
                    .isEnabled(request.isEnabled())
                    .build();
            notificationPreferenceRepository.save(newPreference);
        }
    }
}