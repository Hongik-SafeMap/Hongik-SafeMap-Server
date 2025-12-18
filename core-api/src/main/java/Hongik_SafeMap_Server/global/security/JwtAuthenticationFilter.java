package Hongik_SafeMap_Server.global.security;

import Hongik_SafeMap_Server.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/") || path.startsWith("/signup/")
                || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String accessToken = resolveBearerToken(request);

        if (accessToken != null && tokenUtil.validateToken(accessToken)) {
            setAuthentication(accessToken);
        }

        chain.doFilter(request, response);
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private void setAuthentication(String accessToken) {
        String email = tokenUtil.getEmailFromToken(accessToken);
        String status = tokenUtil.getStatusFromToken(accessToken);

        String role = "ROLE_" + status;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
