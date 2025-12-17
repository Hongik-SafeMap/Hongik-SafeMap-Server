package Hongik_SafeMap_Server.global;

import Hongik_SafeMap_Server.domain.auth.domain.RefreshToken;
import Hongik_SafeMap_Server.domain.auth.repository.RefreshTokenRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.util.CookieUtil;
import Hongik_SafeMap_Server.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // 로그인, 회원가입, 스웨거 등은 필터 통과
        return path.startsWith("/auth/login") || path.startsWith("/signup")
                || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Access Token 추출 (헤더 우선, 없으면 쿠키)
        String accessToken = resolveToken(request);
        String refreshToken = CookieUtil.getCookieValue(request, "Refresh-Token");

        // 2. Access Token 유효성 검사
        if (accessToken != null && tokenUtil.validateToken(accessToken)) {
            setAuthentication(accessToken);
        }
        // 3. Access Token 만료 && Refresh Token 존재 시 -> 재발급 로직
        else if (refreshToken != null && tokenUtil.validateToken(refreshToken)) {
            reissueAccessToken(refreshToken, response);
        }

        chain.doFilter(request, response);
    }

    private void reissueAccessToken(String refreshToken, HttpServletResponse response) {
        String email = tokenUtil.getEmailFromToken(refreshToken);

        // DB에서 해당 유저의 Refresh Token 조회
        RefreshToken storedToken = refreshTokenRepository.findByEmail(email).orElse(null);

        // DB에 토큰이 있고, 쿠키의 토큰과 일치하면 재발급
        if (storedToken != null && storedToken.getRefreshToken().equals(refreshToken)) {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            String newAccessToken = tokenUtil.generateAccessToken(member.getEmail(), member.getName(), member.getStatus());

            // 쿠키/헤더 재설정
            CookieUtil.addAccessTokenCookie(response, newAccessToken);
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            // 인증 객체 설정
            setAuthentication(newAccessToken);
            log.info("Access Token 재발급 완료: {}", email);
        }
    }

    private void setAuthentication(String accessToken) {
        String email = tokenUtil.getEmailFromToken(accessToken);
        String status = tokenUtil.getStatusFromToken(accessToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email, null, List.of(new SimpleGrantedAuthority(status))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return CookieUtil.getCookieValue(request, "Access-Token");
    }
}
