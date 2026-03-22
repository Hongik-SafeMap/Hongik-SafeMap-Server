package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceReportStatus implements EnumUtil.DescriptionProvider {
    IN_PROGRESS("진행 중"),
    WAITING("대기 중"),
    CLOSED("마감");

    @JsonValue
    private final String description;
    
    public static ResourceReportStatus fromDescription(String description) {
        return EnumUtil.fromDescription(ResourceReportStatus.class, description);
    }
}