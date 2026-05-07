package Hongik_SafeMap_Server.domain.terms.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TermsVersionAgreeRequest(
        @Schema(description = "동의할 이용약관 버전", example = "1.0")
        @NotBlank(message = "이용약관 버전은 필수 입력값입니다.")
        @Pattern(regexp = "^\\d+\\.\\d+$", message = "버전은 'X.Y' 형식으로 입력해주세요.")
        String termsVersion,

        @Schema(description = "동의할 개인정보처리방침 버전", example = "1.0")
        @NotBlank(message = "개인정보처리방침 버전은 필수 입력값입니다.")
        @Pattern(regexp = "^\\d+\\.\\d+$", message = "버전은 'X.Y' 형식으로 입력해주세요.")
        String privacyPolicyVersion
) {}
