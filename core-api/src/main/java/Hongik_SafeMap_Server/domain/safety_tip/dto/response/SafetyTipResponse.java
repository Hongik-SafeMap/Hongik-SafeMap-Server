package Hongik_SafeMap_Server.domain.safety_tip.dto.response;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.vo.DisasterType;

import java.time.LocalDateTime;
import java.util.List;

public record SafetyTipResponse(
        Long id,
        DisasterType disasterType,
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
                safetyTip.getDisasterType(),
                safetyTip.getTitle(),
                safetyTip.getDetail(),
                safetyTip.getSupplies().stream()
                        .map(supply -> supply.getContent())
                        .toList(),
                safetyTip.getWarnings().stream()
                        .map(warning -> warning.getContent())
                        .toList(),
                safetyTip.getActions().stream()
                        .map(SafetyActionResponse::of)
                        .toList(),
                safetyTip.getCreatedAt(),
                safetyTip.getUpdatedAt()
        );
    }
}