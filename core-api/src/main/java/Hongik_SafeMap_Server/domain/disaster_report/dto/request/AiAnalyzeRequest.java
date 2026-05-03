package Hongik_SafeMap_Server.domain.disaster_report.dto.request;

public record AiAnalyzeRequest(
        Long reportId,
        String imageUrl
) {}
