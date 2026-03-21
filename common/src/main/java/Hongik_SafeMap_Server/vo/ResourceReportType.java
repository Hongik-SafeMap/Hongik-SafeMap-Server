package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportType {
    REQUEST("요청"),
    SUPPLY("공급");

    @JsonValue
    private final String description;
}