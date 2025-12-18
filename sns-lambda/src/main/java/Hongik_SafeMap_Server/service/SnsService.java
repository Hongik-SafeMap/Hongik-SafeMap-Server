package Hongik_SafeMap_Server.service;

import Hongik_SafeMap_Server.dto.SnsResponse;
import Hongik_SafeMap_Server.vo.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {

    private final WebClient webClient = WebClient.create();

    public SnsResponse verifyToken(String token, LoginType type) {
        if (type == LoginType.KAKAO) {
            return getKakaoUserInfo(token);
        } else if (type == LoginType.GOOGLE) {
            return getGoogleUserInfo(token);
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다: " + type);
    }

    // =================================================================
    // 1) 카카오 사용자 정보 가져오기
    // =================================================================
    @SuppressWarnings("unchecked")
    private SnsResponse getKakaoUserInfo(String accessToken) {
        Map<String, Object> response = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            throw new RuntimeException("카카오 응답이 비어있습니다.");
        }

        String socialId = String.valueOf(response.get("id"));

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        if (kakaoAccount == null) {
            throw new RuntimeException("카카오 kakao_account 정보가 없습니다.");
        }

        String email = (String) kakaoAccount.get("email");

        // 닉네임/이름: profile.nickname 우선
        String name = null;
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile != null) {
            name = (String) profile.get("nickname");
        }
        // fallback: properties.nickname
        if (name == null) {
            Map<String, Object> properties = (Map<String, Object>) response.get("properties");
            if (properties != null) {
                name = (String) properties.get("nickname");
            }
        }

        // phone: 동의/설정에 따라 없을 수 있음 (없으면 null로 유지)
        String phone = (String) kakaoAccount.get("phone_number");

        // email은 카카오에서 동의 안 하면 null일 수 있음 → 이 경우는 서버 정책상 막는 게 보통 안전
        if (email == null || email.isBlank()) {
            throw new RuntimeException("카카오 계정에서 이메일 정보를 제공하지 않았습니다. (email scope 동의 필요)");
        }

        log.info("카카오 인증 성공: email={}, socialId={}, name={}, phone={}", email, socialId, name, phone);
        return new SnsResponse(email, socialId, name, phone);
    }

    // =================================================================
    // 2) 구글 사용자 정보 가져오기
    // =================================================================
    @SuppressWarnings("unchecked")
    private SnsResponse getGoogleUserInfo(String accessToken) {
        Map<String, Object> response = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            throw new RuntimeException("구글 응답이 비어있습니다.");
        }

        // 구글은 sub가 고유 ID
        String socialId = (String) response.get("sub");
        String email = (String) response.get("email");
        String name = (String) response.get("name"); // 보통 제공

        // phone은 기본 userinfo로는 거의 안 옴 (People API 필요)
        String phone = null;

        if (email == null || email.isBlank()) {
            throw new RuntimeException("구글 계정에서 이메일 정보를 제공하지 않았습니다.");
        }
        if (socialId == null || socialId.isBlank()) {
            throw new RuntimeException("구글 계정에서 사용자 식별자(sub) 정보를 가져오지 못했습니다.");
        }

        log.info("구글 인증 성공: email={}, socialId={}, name={}", email, socialId, name);
        return new SnsResponse(email, socialId, name, phone);
    }
}
