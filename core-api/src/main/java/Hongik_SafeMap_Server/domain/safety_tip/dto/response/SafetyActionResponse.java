package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyAction;

public record SafetyActionResponse(
        Long id,
        String title,
        String guide
) {
    public static SafetyActionResponse of(SafetyAction safetyAction) {
        return new SafetyActionResponse(
                safetyAction.getId(),
                safetyAction.getTitle(),
                safetyAction.getGuide()
        );
    }
}