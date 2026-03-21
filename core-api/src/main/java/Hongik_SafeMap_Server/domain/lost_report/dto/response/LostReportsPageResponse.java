package Hongik_SafeMap_Server.domain.lost_report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record LostReportsPageResponse(
        @Schema(description = "게시글 목록")
        List<LostReportResponse> reports,
        
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int currentPage,
        
        @Schema(description = "페이지 크기", example = "10")
        int pageSize,
        
        @Schema(description = "총 게시글 개수", example = "100")
        long totalElements,
        
        @Schema(description = "총 페이지 개수", example = "10")
        int totalPages,
        
        @Schema(description = "첫 번째 페이지 여부", example = "true")
        boolean first,
        
        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
}