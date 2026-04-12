package Hongik_SafeMap_Server.global.dto.response;

import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record MessagePushResponse(
        String name,
        boolean success
) {

    public static MessagePushResponse of(String name, boolean success) {
        return MessagePushResponse.builder()
                .name(name)
                .success(success)
                .build();
    }
}