package Hongik_SafeMap_Server.service;

import Hongik_SafeMap_Server.dto.SnsResponse;
import Hongik_SafeMap_Server.vo.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {
    private final WebClient webClient = WebClient.create();

    public SnsResponse verifyToken(String token, LoginType type) {
        if (type == LoginType.카카오) {
            return getKakaoUserInfo(token);
        } else if (type == LoginType.구글) {
            return getGoogleUserInfo(token);
        } else {
            throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다: " + type);
        }
    }

    // =================================================================
    // 1. 카카오 사용자 정보 가져오기
    // =================================================================
    private SnsResponse getKakaoUserInfo(String accessToken) {
        // 카카오 API 호출
        Map<String, Object> response = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // 람다는 동기로 처리해도 무방함

        if (response == null) throw new RuntimeException("카카오 응답이 비어있습니다.");

        // 데이터 파싱 (카카오는 id와 kakao_account.email에 정보가 있음)
        String socialId = String.valueOf(response.get("id"));

        Map<String, Object> account = (Map<String, Object>) response.get("kakao_account");
        String email = (String) account.get("email");

        log.info("카카오 인증 성공: {} / {}", email, socialId);
        return new SnsResponse(email, socialId);
    }

    // =================================================================
    // 2. 구글 사용자 정보 가져오기
    // =================================================================
    private SnsResponse getGoogleUserInfo(String accessToken) {
        // 구글 API 호출
        Map<String, Object> response = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) throw new RuntimeException("구글 응답이 비어있습니다.");

        // 구글은 sub가 ID, email이 이메일
        String socialId = (String) response.get("sub");
        String email = (String) response.get("email");

        log.info("구글 인증 성공: {} / {}", email, socialId);
        return new SnsResponse(email, socialId);
    }
}
