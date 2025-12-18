package Hongik_SafeMap_Server.domain.member.sensitive_info.dto;

import Hongik_SafeMap_Server.domain.member.sensitive_info.domain.SensitiveInfo;

public record SensitiveInfoResponse(
        Long sensitiveInfoId,
        String bloodType,
        String allergies,
        String chronicDiseases,
        String medications
) {
    public static SensitiveInfoResponse of(SensitiveInfo sensitiveInfo) {
        return new SensitiveInfoResponse(
                sensitiveInfo.getId(),
                sensitiveInfo.getBloodType(),
                sensitiveInfo.getAllergies(),
                sensitiveInfo.getChronicDiseases(),
                sensitiveInfo.getMedications()
        );
    }
}
