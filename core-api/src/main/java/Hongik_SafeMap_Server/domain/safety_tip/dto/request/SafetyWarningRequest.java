package Hongik_SafeMap_Server.domain.safety_tip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SafetyWarningRequest(
        @NotBlank
        @Size(max = 100)
        String content
) {
}