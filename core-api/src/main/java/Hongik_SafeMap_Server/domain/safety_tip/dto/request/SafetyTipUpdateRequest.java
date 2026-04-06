package Hongik_SafeMap_Server.domain.safety_tip.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SafetyTipUpdateRequest(
        @NotBlank
        @Size(max = 20)
        String title,

        @NotBlank
        @Size(max = 1000)
        String detail,

        List<@NotBlank @Size(max = 100) String> supplies,

        List<@NotBlank @Size(max = 100) String> warnings,

        @NotEmpty
        @Valid
        List<SafetyActionRequest> actions
) {
}