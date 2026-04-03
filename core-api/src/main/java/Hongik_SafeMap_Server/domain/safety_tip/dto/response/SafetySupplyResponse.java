package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetySupply;

public record SafetySupplyResponse(
        Long id,
        String content
) {
    public static SafetySupplyResponse of(SafetySupply safetySupply) {
        return new SafetySupplyResponse(
                safetySupply.getId(),
                safetySupply.getContent()
        );
    }
}