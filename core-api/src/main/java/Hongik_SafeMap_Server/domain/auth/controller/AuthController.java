package Hongik_SafeMap_Server.domain.auth.controller;

import Hongik_SafeMap_Server.domain.auth.SnsLambdaClient;
import Hongik_SafeMap_Server.domain.auth.dto.LoginRequest;
import Hongik_SafeMap_Server.domain.auth.dto.LoginResponse;
import Hongik_SafeMap_Server.domain.auth.dto.SnsLoginRequest;
import Hongik_SafeMap_Server.domain.auth.service.AuthService;
import Hongik_SafeMap_Server.dto.SnsAuthResponse;
import Hongik_SafeMap_Server.util.CookieUtil;
import Hongik_SafeMap_Server.util.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SnsLambdaClient snsLambdaClient;

    // 1. 일반 로그인
    @PostMapping("/login/general")
    public ResponseEntity<LoginResponse> generalLogin(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response // 쿠키 설정을 위해 필요
    ) {
        // 서비스 호출
        LoginResponse loginResponse = authService.generalLogin(request.email(), request.password());

        // Refresh Token을 쿠키에 설정 (HttpOnly)
        CookieUtil.addRefreshTokenCookie(response, loginResponse.refreshToken());

        return ResponseEntity.ok(loginResponse);
    }

    // 2. SNS 로그인
    @PostMapping("/login/sns")
    public ResponseEntity<LoginResponse> snsLogin(
            @Valid @RequestBody SnsLoginRequest request,
            HttpServletResponse response
    ) {
        // ★ 2. 람다 호출해서 진짜 이메일/SocialId 받아오기
        SnsAuthResponse snsData = snsLambdaClient.callLambda(request);

        // ★ 3. 받아온 진짜 데이터로 서비스 호출
        LoginResponse loginResponse = authService.snsLogin(
                snsData.getEmail(),      // 람다가 준 이메일
                snsData.getSocialId(),   // 람다가 준 ID
                request.loginType()      // 요청받은 타입
        );

        CookieUtil.addRefreshTokenCookie(response, loginResponse.refreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    // 3. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal String email,
            HttpServletResponse response
    ) {
        // DB에서 Refresh Token 삭제 (재발급 차단)
        if (email != null) {
            authService.logout(email);
        }

        // 클라이언트 쿠키 삭제 (Access, Refresh 둘 다 삭제)
        CookieUtil.deleteCookie(response, "Access-Token");
        CookieUtil.deleteCookie(response, "Refresh-Token");

        return ResponseEntity.ok().build();
    }

}
