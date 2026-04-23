package Hongik_SafeMap_Server.domain.admin.disaster_review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AdminReportPageResponse(
        @Schema(description = "제보 목록")
        List<AdminReportResponse> reports,
        
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int currentPage,
        
        @Schema(description = "페이지 크기", example = "20")
        int pageSize,
        
        @Schema(description = "총 제보 개수", example = "100")
        long totalElements,
        
        @Schema(description = "총 페이지 개수", example = "5")
        int totalPages,
        
        @Schema(description = "첫 번째 페이지 여부", example = "true")
        boolean first,
        
        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
}