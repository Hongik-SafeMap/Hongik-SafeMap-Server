package Hongik_SafeMap_Server.domain.auth;

import Hongik_SafeMap_Server.domain.auth.dto.request.SnsLoginRequest;
import Hongik_SafeMap_Server.dto.SnsAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SnsLambdaClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public SnsAuthResponse callLambda(SnsLoginRequest request) {
        // 로컬 테스트용 주소 (람다 모듈을 8081로 띄웠을 때)
        // 실제 AWS 배포시에는 AWS Lambda URL로 교체해야 함 (application.yml로 관리 추천)
        String lambdaUrl = "http://localhost:8081/snsAuth";

        // 람다로 POST 요청 전송 (토큰과 타입을 줌)
        // 람다에서 검증 후 이메일/ID 반환 (SnsAuthResponse)
        return restTemplate.postForObject(lambdaUrl, request, SnsAuthResponse.class);
    }
}
