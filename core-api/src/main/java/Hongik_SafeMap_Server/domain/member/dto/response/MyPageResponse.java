package Hongik_SafeMap_Server.domain.member.dto.response;

import Hongik_SafeMap_Server.domain.member.domain.Member;

public record MyPageResponse(
        String name,
        String email,
        String phone,
        String status
){
    public static MyPageResponse of(Member member){
        return new MyPageResponse(
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus().getDescription()
        );
    }
}
