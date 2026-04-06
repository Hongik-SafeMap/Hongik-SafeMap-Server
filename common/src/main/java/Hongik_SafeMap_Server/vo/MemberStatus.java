package Hongik_SafeMap_Server.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    USER("일반"),
    ADMIN("관리자");

    private final String description;
}
