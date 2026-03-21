package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportCategory {
    FOOD("식량"),
    WATER("식수"),
    MEDICINE("의약품"),
    SHELTER("대피처"),
    CLOTHING("의류"),
    TOOLS("도구");

    @JsonValue
    private final String description;
}