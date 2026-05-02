package Hongik_SafeMap_Server.domain.notification.dto.response;

import Hongik_SafeMap_Server.domain.notification.domain.Notification;
import lombok.Builder;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record NotificationResponse(
        Long id,
        String title,
        String content,
        boolean isRead,
        LocalDateTime createdAt
) {

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
