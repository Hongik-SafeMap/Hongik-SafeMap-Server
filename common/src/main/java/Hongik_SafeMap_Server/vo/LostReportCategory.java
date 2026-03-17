package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LostReportCategory {
    PERSON("사람"),
    PET("반려동물"),
    BELONGINGS("소지품");

    @JsonValue
    private final String description;
}