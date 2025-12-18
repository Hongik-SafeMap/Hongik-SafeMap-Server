package Hongik_SafeMap_Server.domain.admin.account.service;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

    private final MemberUtil memberUtil;

    public AdminMyPageResponse getAdminMyPage() {
        Member member = memberUtil.getLoggedInMember();
        return AdminMyPageResponse.of(member);
    }
}
