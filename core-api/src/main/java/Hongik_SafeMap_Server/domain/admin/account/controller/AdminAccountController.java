package Hongik_SafeMap_Server.domain.admin.account.controller;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.PromoteToAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.service.AdminAccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "관리자 계정 추가", description = "이메일과 관리자용 닉네임을 등록하여 관리자 계정을 추가합니다.")
    @PostMapping("/promote")
    public ResponseEntity<Void> promoteToAdmin(@Valid @RequestBody PromoteToAdminRequest request) {
        adminAccountService.promoteToAdmin(request);
        return ResponseEntity.ok().build();
    }
}
