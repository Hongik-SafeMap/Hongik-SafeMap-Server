package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.vo.DisasterType;

public record SafetyTipSummaryResponse(
        Long id,
        DisasterType disasterType,
        String title,
        String detail
) {
    public static SafetyTipSummaryResponse of(SafetyTip safetyTip) {
        return new SafetyTipSummaryResponse(
                safetyTip.getId(),
                safetyTip.getDisasterType(),
                safetyTip.getTitle(),
                safetyTip.getDetail()
        );
    }
}