package Hongik_SafeMap_Server.domain.terms.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record TermsPageResponse(
        @Schema(description = "이용약관 버전 목록")
        List<TermsDetailListResponse> terms,

        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        int currentPage,

        @Schema(description = "페이지 크기", example = "10")
        int pageSize,

        @Schema(description = "총 버전 개수", example = "5")
        long totalElements,

        @Schema(description = "총 페이지 개수", example = "1")
        int totalPages,

        @Schema(description = "첫 번째 페이지 여부", example = "true")
        boolean first,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
    public static TermsPageResponse of(Page<TermsDetailListResponse> page) {
        return new TermsPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
