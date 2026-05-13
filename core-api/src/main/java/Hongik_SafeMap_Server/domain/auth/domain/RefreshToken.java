package Hongik_SafeMap_Server.domain.auth.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refresh_token", timeToLive = 1296000) // 15 days
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public void updateToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}
