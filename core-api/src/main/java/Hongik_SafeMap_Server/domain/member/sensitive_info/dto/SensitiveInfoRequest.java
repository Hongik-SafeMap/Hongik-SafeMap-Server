package Hongik_SafeMap_Server.domain.member.sensitive_info.dto;

import jakarta.validation.constraints.Size;

public record SensitiveInfoRequest(
        @Size(max = 10)
        String bloodType,

        @Size(max = 500)
        String allergies,

        @Size(max = 500)
        String chronicDiseases,

        @Size(max = 500)
        String medications
) {
}
