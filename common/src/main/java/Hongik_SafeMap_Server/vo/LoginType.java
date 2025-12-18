package Hongik_SafeMap_Server.vo;

import Hongik_SafeMap_Server.exception.MemberException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static Hongik_SafeMap_Server.exception.ErrorMessage.INVALID_LOGIN_TYPE;

@Getter
@AllArgsConstructor
public enum LoginType {
    GENERAL("일반"),
    KAKAO("카카오"),
    GOOGLE("구글");

    @JsonValue
    private final String description;

    public static LoginType from(String value) {
        for (LoginType type : LoginType.values()) {
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new MemberException(INVALID_LOGIN_TYPE);
    }
}
