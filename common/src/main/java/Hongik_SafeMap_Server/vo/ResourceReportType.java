package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportType implements EnumUtil.DescriptionProvider {
    REQUEST("요청"),
    SUPPLY("공급");

    @JsonValue
    private final String description;
    
    public static ResourceReportType fromDescription(String description) {
        return EnumUtil.fromDescription(ResourceReportType.class, description);
    }
}