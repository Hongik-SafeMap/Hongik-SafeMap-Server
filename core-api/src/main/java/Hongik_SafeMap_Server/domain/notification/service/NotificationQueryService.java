package Hongik_SafeMap_Server.domain.notification.service;

import Hongik_SafeMap_Server.domain.notification.domain.Notification;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationPageResponse;
import Hongik_SafeMap_Server.domain.notification.dto.response.NotificationResponse;
import Hongik_SafeMap_Server.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationPageResponse getNotifications(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        val notificationPage = notificationRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
        
        val notifications = notificationPage.getContent().stream()
                .map(NotificationResponse::from)
                .toList();
        
        return new NotificationPageResponse(
                notifications,
                notificationPage.getNumber(),
                notificationPage.getSize(),
                notificationPage.getTotalElements(),
                notificationPage.getTotalPages(),
                notificationPage.isFirst(),
                notificationPage.isLast()
        );
    }

    public long getUnreadCount(Long memberId) {
        return notificationRepository.countByMemberIdAndIsReadFalse(memberId);
    }

    @Transactional
    public void markAsRead(Long memberId) {
        List<Notification> notifications = notificationRepository.findNotificationByMemberIdAndIsReadFalse(memberId);
        
        notifications.forEach(Notification::markAsRead);
    }
}