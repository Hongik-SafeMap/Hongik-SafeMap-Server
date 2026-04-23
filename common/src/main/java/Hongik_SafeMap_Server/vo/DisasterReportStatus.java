package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisasterReportStatus implements EnumUtil.DescriptionProvider {
    PENDING("검토대기"),
    APPROVED("승인"),
    BLINDED("블라인드");

    @JsonValue
    private final String description;

    @JsonCreator
    public static DisasterReportStatus fromDescription(String description) {
        return EnumUtil.fromDescription(DisasterReportStatus.class, description);
    }
}
