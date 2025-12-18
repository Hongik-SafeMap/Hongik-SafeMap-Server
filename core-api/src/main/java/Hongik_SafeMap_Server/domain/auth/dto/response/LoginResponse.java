package Hongik_SafeMap_Server.domain.auth.dto.response;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.LoginType;
import Hongik_SafeMap_Server.vo.MemberStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String name,
        String email,
        MemberStatus status,
        LoginType loginType
) {
    public static LoginResponse of(Member member, String accessToken, String refreshToken) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                member.getName(),
                member.getEmail(),
                member.getStatus(),
                member.getLoginType());
    }
}
