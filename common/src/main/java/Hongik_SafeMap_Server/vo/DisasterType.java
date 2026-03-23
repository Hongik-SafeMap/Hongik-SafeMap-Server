package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisasterType implements EnumUtil.DescriptionProvider {
    FIRE("화재"),
    EARTHQUAKE("지진"),
    FLOOD("홍수"),
    LANDSLIDE("산사태"),
    TYPHOON("태풍"),
    ETC("기타");

    @JsonValue
    private final String description;
    
    public static DisasterType fromDescription(String description) {
        return EnumUtil.fromDescription(DisasterType.class, description);
    }
}
