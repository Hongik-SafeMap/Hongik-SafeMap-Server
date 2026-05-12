package Hongik_SafeMap_Server.domain.auth.dto.response;

public record SnsAuthResponse(
        String email,
        String socialId,
        String name,
        String phone
) {
}
