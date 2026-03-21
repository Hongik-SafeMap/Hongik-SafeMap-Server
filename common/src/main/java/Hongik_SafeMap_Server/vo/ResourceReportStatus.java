package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportStatus {
    IN_PROGRESS("진행중"),
    WAITING("대기중"),
    RESOLVED("해결됨");

    @JsonValue
    private final String description;
}