package Hongik_SafeMap_Server.domain.disaster_type.dto.request;

import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DisasterTypeUpdateRequest(
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(max = 500)
        String iconUrl,

        @NotNull
        @Valid
        SafetyTipUpdateRequest safetyTip
) {}
