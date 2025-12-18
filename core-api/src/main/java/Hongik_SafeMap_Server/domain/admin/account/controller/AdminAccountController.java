package Hongik_SafeMap_Server.domain.admin.account.controller;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.service.AdminAccountService;
import Hongik_SafeMap_Server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAccountController {
    private final AdminAccountService adminAccountService;

    @GetMapping("/me")
    public ResponseEntity<AdminMyPageResponse> getAdminMyPage() {
        AdminMyPageResponse adminMyPageResponse = adminAccountService.getAdminMyPage();
        return ResponseEntity.ok().body(adminMyPageResponse);
    }
}
