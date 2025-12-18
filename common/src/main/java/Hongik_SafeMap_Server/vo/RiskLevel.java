package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RiskLevel {
    EMERGENCY("긴급"),
    HIGH("높음"),
    MEDIUM("보통"),
    LOW("낮음");

    @JsonValue
    private final String description;
}
