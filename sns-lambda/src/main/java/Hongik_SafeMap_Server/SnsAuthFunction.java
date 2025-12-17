package Hongik_SafeMap_Server;

import Hongik_SafeMap_Server.dto.SnsRequest;
import Hongik_SafeMap_Server.dto.SnsResponse;
import Hongik_SafeMap_Server.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SnsAuthFunction {
    private final SnsService snsService;

    // 함수 이름: snsAuth (이 이름이 URL 경로가 됨 -> localhost:8081/snsAuth)
    @Bean
    public Function<SnsRequest, SnsResponse> snsAuth() {
        return request -> {
            log.info("SNS 로그인 요청 받음: Type={}, Token={}", request.loginType(), request.token());

            // 서비스 호출해서 검증 결과 리턴
            return snsService.verifyToken(request.token(), request.loginType());
        };
    }
}
