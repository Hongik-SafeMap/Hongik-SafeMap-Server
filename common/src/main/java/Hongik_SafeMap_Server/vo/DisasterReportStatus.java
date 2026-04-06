package Hongik_SafeMap_Server.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisasterReportStatus {
    PENDING("검토대기"),
    APPROVED("승인"),
    BLINDED("블라인드");

    @JsonValue
    private final String description;
}
