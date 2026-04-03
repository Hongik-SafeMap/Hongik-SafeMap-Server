package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyWarning;

public record SafetyWarningResponse(
        Long id,
        String content
) {
    public static SafetyWarningResponse of(SafetyWarning safetyWarning) {
        return new SafetyWarningResponse(
                safetyWarning.getId(),
                safetyWarning.getContent()
        );
    }
}