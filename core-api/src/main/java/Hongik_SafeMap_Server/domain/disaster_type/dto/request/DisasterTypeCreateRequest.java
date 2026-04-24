package Hongik_SafeMap_Server.domain.disaster_type.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DisasterTypeCreateRequest(
        @NotBlank
        @Size(max = 50)
        String name,

        @Size(max = 500)
        String iconUrl
) {}
