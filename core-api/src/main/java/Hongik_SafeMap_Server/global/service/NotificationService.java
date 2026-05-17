package Hongik_SafeMap_Server.global.service;

import Hongik_SafeMap_Server.domain.notification.domain.Notification;
import Hongik_SafeMap_Server.domain.notification.repository.NotificationPreferenceRepository;
import Hongik_SafeMap_Server.domain.notification.repository.NotificationRepository;
import Hongik_SafeMap_Server.global.dto.request.MessagePushServiceRequest;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    public void sendDisasterReportNotification(DisasterType disasterType, String locationName) {
        if (!disasterType.isNotificationEnabled()) {
            log.info("재난 유형 {} 알림이 관리자에 의해 비활성화되어 있습니다", disasterType.getName());
            return;
        }

        // 해당 재난 유형에 대해 알림을 활성화한 사용자들 조회
        var enabledPreferences = notificationPreferenceRepository
                .findByDisasterTypeAndIsEnabledTrue(disasterType);

        if (enabledPreferences.isEmpty()) {
            log.info("재난 유형 {} 알림을 설정한 사용자가 없습니다", disasterType.getName());
            return;
        }

        String title = String.format("새로운 %s 재난 제보", disasterType.getName());
        String content = String.format("%s에 %s 관련 제보가 등록되었습니다",
                locationName, disasterType.getName()); // 서울특별시 강남구 역삼동에 화재 관련 제보가 등록되었습니다.

        // 각 사용자에게 푸시 알림 전송
        int successCount = 0;

        for (var preference : enabledPreferences) {
            String fcmToken = preference.getMember().getFcmToken();

            // 알림 이력 저장
            Notification notification = Notification.of(preference.getMember(), title, content);
            notificationRepository.save(notification);

            if (fcmToken != null && !fcmToken.isEmpty()) {
                try {
                    fcmService.pushMessage(MessagePushServiceRequest.of(fcmToken, title, content));
                    log.info("사용자 {}에게 {} 재난 알림 전송 완료",
                            preference.getMember().getEmail(), disasterType.getName());
                    successCount++;
                } catch (Exception e) {
                    log.error("사용자 {}에게 알림 전송 실패: {}",
                            preference.getMember().getEmail(), e.getMessage());
                }
            }
        }

        log.info("재난 유형 {} 알림 전송 완료: 성공 {}명, 실패 {}명",
                disasterType.getName(), successCount, enabledPreferences.size() - successCount);
    }
}