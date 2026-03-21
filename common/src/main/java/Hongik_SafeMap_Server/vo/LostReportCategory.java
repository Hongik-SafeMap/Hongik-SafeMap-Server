package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LostReportCategory implements EnumUtil.DescriptionProvider {
    PERSON("사람"),
    PET("반려동물"),
    BELONGINGS("소지품");

    @JsonValue
    private final String description;
    
    public static LostReportCategory fromDescription(String description) {
        return EnumUtil.fromDescription(LostReportCategory.class, description);
    }
}