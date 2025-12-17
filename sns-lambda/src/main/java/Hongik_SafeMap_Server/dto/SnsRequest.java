package Hongik_SafeMap_Server.dto;

import Hongik_SafeMap_Server.vo.LoginType;

public record SnsRequest(
        String token,
        LoginType loginType
) {
}
