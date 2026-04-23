package Hongik_SafeMap_Server.domain.disaster_type.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DisasterTypeUpdateRequest(
        @NotBlank
        @Size(max = 50)
        String name
) {}
