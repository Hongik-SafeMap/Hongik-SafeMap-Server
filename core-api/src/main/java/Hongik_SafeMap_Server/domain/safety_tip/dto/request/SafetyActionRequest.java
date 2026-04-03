package Hongik_SafeMap_Server.domain.safety_tip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SafetyActionRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 1000)  
        String guide
) {}