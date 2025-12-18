package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisasterType {
    FIRE("화재"),
    EARTHQUAKE("지진"),
    FLOOD("홍수"),
    LANDSLIDE("산사태"),
    TYPHOON("태풍"),
    ETC("기타");

    @JsonValue
    private final String description;
}
