package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;

public record SafetyTipSummaryResponse(
        Long id,
        DisasterTypeResponse disasterType,
        String title,
        String detail
) {
    public static SafetyTipSummaryResponse of(SafetyTip safetyTip) {
        return new SafetyTipSummaryResponse(
                safetyTip.getId(),
                DisasterTypeResponse.of(safetyTip.getDisasterType()),
                safetyTip.getTitle(),
                safetyTip.getDetail()
        );
    }
}
