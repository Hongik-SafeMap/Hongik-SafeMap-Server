package Hongik_SafeMap_Server.domain.safety_tip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SafetySupplyRequest(
        @NotBlank
        @Size(max = 100)
        String content
) {
}