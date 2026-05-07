package Hongik_SafeMap_Server.domain.privacy_policy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record PrivacyPolicyPageResponse(
        @Schema(description = "개인정보처리방침 버전 목록")
        List<PrivacyPolicyDetailListResponse> policies,

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
    public static PrivacyPolicyPageResponse of(Page<PrivacyPolicyDetailListResponse> page) {
        return new PrivacyPolicyPageResponse(
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
