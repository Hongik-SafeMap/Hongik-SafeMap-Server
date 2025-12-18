package Hongik_SafeMap_Server.domain.admin.member.dto;

public record AdminMemberResponse(
        Long id,
        String name,
        String email,
        long reportCount,
        int accuracy,
        boolean isCredible
) {}
