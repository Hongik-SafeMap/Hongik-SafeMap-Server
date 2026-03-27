package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisasterReportEvaluationType implements EnumUtil.DescriptionProvider {
    HELPFUL("도움됨"),
    NOT_HELPFUL("도움 안됨"),
    ACCURATE("정확함"),
    FALSE_REPORT("허위제보");

    @JsonValue
    private final String description;
    
    public static DisasterReportEvaluationType fromDescription(String description) {
        return EnumUtil.fromDescription(DisasterReportEvaluationType.class, description);
    }
}