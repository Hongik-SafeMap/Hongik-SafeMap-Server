package Hongik_SafeMap_Server.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {

    private final String presignedUrl;
    private final String imageUrl;
    private final LocalDateTime expiresAt;
}