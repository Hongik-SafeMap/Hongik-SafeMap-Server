package Hongik_SafeMap_Server.domain.disaster_report.service;

import Hongik_SafeMap_Server.domain.disaster_report.dto.request.AiAnalyzeRequest;
import Hongik_SafeMap_Server.domain.disaster_report.dto.response.AiAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AiAnalyzeClient {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public AiAnalyzeResponse analyze(Long reportId, String imageUrl) {
        AiAnalyzeRequest request = new AiAnalyzeRequest(reportId, imageUrl);

        return webClient.post()
                .uri(aiServerUrl + "/api/ai/analyze-report")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiAnalyzeResponse.class)
                .block();
    }
}
