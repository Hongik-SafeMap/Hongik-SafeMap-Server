package Hongik_SafeMap_Server.global.dto.request;

import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record MessagePushRequest(
        boolean validateOnly,
        MessageRequest message
) {

    public static MessagePushRequest of(MessagePushServiceRequest request, boolean validateOnly) {
        return MessagePushRequest.builder()
                .validateOnly(validateOnly) // 개발 환경에서만 validateOnly 모드 활성화
                .message(MessageRequest.of(request))
                .build();
    }

    @Builder(access = PRIVATE)
    record MessageRequest(
            NotificationRequest notification,
            String token
    ) {

        private static MessageRequest of(MessagePushServiceRequest request) {
            return MessageRequest.builder()
                    .notification(NotificationRequest.of(request))
                    .token(request.targetToken())
                    .build();
        }
    }

    @Builder(access = PRIVATE)
    record NotificationRequest(
            String title,
            String body
    ) {

        private static NotificationRequest of(MessagePushServiceRequest request) {
            return NotificationRequest.builder()
                    .title(request.title())
                    .body(request.body())
                    .build();
        }
    }
}