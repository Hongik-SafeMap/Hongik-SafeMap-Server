package Hongik_SafeMap_Server.domain.admin.account.dto.response;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminAccountResponse {
    private Long id;
    private String nickname;
    private String email;

    public static AdminAccountResponse of(Member member) {
        return AdminAccountResponse.builder()
                .id(member.getId())
                .nickname(member.getAdminNickname())
                .email(member.getEmail())
                .build();
    }
}