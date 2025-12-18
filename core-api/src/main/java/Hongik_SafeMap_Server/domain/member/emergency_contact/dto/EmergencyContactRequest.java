package Hongik_SafeMap_Server.domain.member.emergency_contact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmergencyContactRequest(
        @NotBlank
        @Size(max = 50)
        String name,

        @Size(max = 30)
        String relationship,

        @NotBlank
        @Size(max = 30)
        String phone
) {}
