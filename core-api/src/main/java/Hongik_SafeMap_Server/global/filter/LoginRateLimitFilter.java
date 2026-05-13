package Hongik_SafeMap_Server.global.filter;

import Hongik_SafeMap_Server.exception.ErrorMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final Set<String> RATE_LIMITED_PATHS = Set.of(
            "/auth/login/general",
            "/auth/login/sns"
    );
    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 300;

    private final StringRedisTemplate redisTemplate;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.POST.matches(request.getMethod())
                || !RATE_LIMITED_PATHS.contains(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String ip = resolveClientIp(request);
        String key = "login_rate_limit:" + ip;
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_SECONDS * 1000; // (WINDOW_SECONDS)초 전 시각

        // 최근 5분 이내 실패 횟수 확인
        Long recentFailures = redisTemplate.opsForZSet().count(key, windowStart, now);
        if (recentFailures != null && recentFailures >= MAX_ATTEMPTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 5회 초과 시 429 응답 반환 후 요청 차단
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"" + ErrorMessage.LOGIN_RATE_LIMIT_EXCEEDED + "\"}");
            return;
        }

        chain.doFilter(request, response);

        // 실패 시 타임스탬프 기록
        if (response.getStatus() != HttpStatus.OK.value()) {
            redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart); // 오래된 항목 제거 후 키 만료 설정
            redisTemplate.opsForZSet().add(key, String.valueOf(now), now); // 실패 시각 기록
            redisTemplate.expire(key, WINDOW_SECONDS, java.util.concurrent.TimeUnit.SECONDS); // 마지막 실패 시점 기준 Key 만료 설정
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For"); // 프록시를 거친 경우
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim(); // 첫 번째 IP가 실제 클라이언트 IP
        }
        return request.getRemoteAddr(); // 프록시 없이 직접 연결된 경우
    }
}
