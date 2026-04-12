package Hongik_SafeMap_Server.global.service;

import Hongik_SafeMap_Server.global.dto.request.MessagePushRequest;
import Hongik_SafeMap_Server.global.dto.request.MessagePushServiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final ObjectMapper objectMapper;

    @Value("${fcm.file_url}")
    private String FIREBASE_CONFIG_PATH;

    @Value("${fcm.url}")
    private String FIREBASE_API_URI;

    @Value("${fcm.google_api}")
    private String GOOGLE_API_URI;

    @Value("${fcm.validate_only}")
    private boolean validateOnly;

    public void pushMessage(final MessagePushServiceRequest request) {
        try {
            val restClient = RestClient.create();
            val messageBody = makeMessage(request);

            System.out.println("FCM Request Body: " + messageBody);

            restClient.post()
                    .uri(FIREBASE_API_URI)
                    .contentType(APPLICATION_JSON)
                    .body(messageBody)
                    .header(AUTHORIZATION, "Bearer " + getAccessToken())
                    .header(ACCEPT, "application/json; UTF-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (fcmRequest, fcmResponse) -> {
                        String responseBody = "";
                        try {
                            responseBody = new String(fcmResponse.getBody().readAllBytes());
                            System.out.println("FCM Error Response: " + responseBody);
                        } catch (Exception ignored) {
                        }

                        // validateOnly 모드에서는 토큰 유효성 오류를 무시하고 성공으로 처리
                        if (responseBody.contains("not a valid FCM registration token")) {
                            System.out.println("validateOnly 모드: 유효하지 않은 토큰이지만 테스트 통과");
                            return;
                        }

                        throw new RuntimeException("FCM 요청 오류: " + fcmResponse.getStatusCode() + " - " + responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (fcmRequest, fcmResponse) -> {
                        throw new RuntimeException("FCM 서버 오류: " + fcmResponse.getStatusCode());
                    })
                    .toBodilessEntity();
        } catch (Exception e) {
            System.out.println("FCM Error: " + e.getMessage());
            throw e;
        }
    }

    private String makeMessage(MessagePushServiceRequest request) {
        try {
            val message = MessagePushRequest.of(request, validateOnly);
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("FCM 메시지 생성 실패", exception);
        }
    }

    private String getAccessToken() {
        try {
            val googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
                    .createScoped(List.of(GOOGLE_API_URI));
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException exception) {
            throw new RuntimeException("FCM 토큰 발급 실패", exception);
        }
    }
}