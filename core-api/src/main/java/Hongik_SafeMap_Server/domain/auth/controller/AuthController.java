package Hongik_SafeMap_Server.domain.auth.controller;

import Hongik_SafeMap_Server.domain.auth.dto.request.LoginRequest;
import Hongik_SafeMap_Server.domain.auth.dto.request.TokenReissueRequest;
import Hongik_SafeMap_Server.domain.auth.dto.response.LoginResponse;
import Hongik_SafeMap_Server.domain.auth.dto.request.SnsLoginRequest;
import Hongik_SafeMap_Server.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1) 일반 로그인 (✅ 쿠키 X, 토큰을 바디로 반환)
    @PostMapping("/login/general")
    public ResponseEntity<LoginResponse> generalLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.generalLogin(request.email(), request.password(), request.fcmToken()));
    }

    // 2) SNS 로그인 (✅ 쿠키 X)
    @PostMapping("/login/sns")
    public ResponseEntity<LoginResponse> snsLogin(@Valid @RequestBody SnsLoginRequest request) {
        return ResponseEntity.ok(authService.processSnsLogin(request));
    }

    // 3) 토큰 재발급 (✅ refreshToken 바디로 받음)
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@Valid @RequestBody TokenReissueRequest request) {
        return ResponseEntity.ok(authService.reissue(request.refreshToken()));
    }

    // 4) 로그아웃 (✅ DB refresh 삭제만)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal String email) {
        if (email != null) {
            authService.logout(email);
        }
        return ResponseEntity.ok().build();
    }

}
