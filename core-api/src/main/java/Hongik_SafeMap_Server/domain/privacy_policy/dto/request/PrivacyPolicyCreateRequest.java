package Hongik_SafeMap_Server.domain.privacy_policy.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record PrivacyPolicyCreateRequest(
        @Schema(description = "개인정보처리방침 버전", example = "1.0")
        @NotBlank(message = "버전은 필수 입력값입니다.")
        @Pattern(regexp = "^\\d+\\.\\d+$", message = "버전은 'X.Y' 형식으로 입력해주세요.")
        String version,

        @Schema(description = "개인정보처리방침 문서 제목", example = "세이프맵 개인정보처리방침")
        @NotBlank(message = "문서 제목은 필수 입력값입니다.")
        String title,

        @Schema(description = "시행일", example = "2026-05-01")
        @NotNull(message = "시행일은 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "등록할 개인정보처리방침 항목 목록")
        @NotEmpty(message = "항목 목록은 비어있을 수 없습니다.")
        @Valid
        List<PrivacyPolicyCreateItemRequest> sections
) {
    public record PrivacyPolicyCreateItemRequest(
            @Schema(description = "항목 헤더", example = "1. 수집하는 개인정보의 항목")
            @NotBlank(message = "항목 헤더는 필수 입력값입니다.")
            String header,

            @Schema(description = "항목 내용", example = "서비스 제공을 위해 아래와 같은 개인정보를 수집합니다...")
            @NotBlank(message = "항목 내용은 필수 입력값입니다.")
            String content
    ) {}
}
