package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;

import java.time.LocalDateTime;
import java.util.List;

public record SafetyTipResponse(
        Long id,
        DisasterTypeResponse disasterType,
        String title,
        String detail,
        List<String> supplies,
        List<String> warnings,
        List<SafetyActionResponse> actions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SafetyTipResponse of(SafetyTip safetyTip) {
        return new SafetyTipResponse(
                safetyTip.getId(),
                DisasterTypeResponse.of(safetyTip.getDisasterType()),
                safetyTip.getTitle(),
                safetyTip.getDetail(),
                safetyTip.getSupplies().stream().toList(),
                safetyTip.getWarnings().stream().toList(),
                safetyTip.getActions().stream()
                        .map(SafetyActionResponse::of)
                        .toList(),
                safetyTip.getCreatedAt(),
                safetyTip.getUpdatedAt()
        );
    }
}
