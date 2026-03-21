package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportCategory implements EnumUtil.DescriptionProvider {
    FOOD("식량"),
    WATER("식수"),
    MEDICINE("의약품"),
    SHELTER("대피처"),
    CLOTHING("의류"),
    TOOLS("도구");

    @JsonValue
    private final String description;
    
    public static ResourceReportCategory fromDescription(String description) {
        return EnumUtil.fromDescription(ResourceReportCategory.class, description);
    }
}