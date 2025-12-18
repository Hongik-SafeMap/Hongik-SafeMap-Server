package Hongik_SafeMap_Server.domain.admin.account.dto;

import Hongik_SafeMap_Server.domain.member.domain.Member;

public record AdminMyPageResponse(
        String name,
        String email
) {
    public static AdminMyPageResponse of(Member member) {
        return new AdminMyPageResponse(member.getName(), member.getEmail());
    }
}
