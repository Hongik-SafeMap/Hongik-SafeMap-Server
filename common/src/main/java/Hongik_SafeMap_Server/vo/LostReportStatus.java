package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LostReportStatus implements EnumUtil.DescriptionProvider {
    SEARCHING("찾는 중"),
    FOUND("발견됨");

    @JsonValue
    private final String description;
    
    public static LostReportStatus fromDescription(String description) {
        return EnumUtil.fromDescription(LostReportStatus.class, description);
    }
}