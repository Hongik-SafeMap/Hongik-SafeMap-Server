package Hongik_SafeMap_Server.domain.admin.account.controller;

import Hongik_SafeMap_Server.domain.admin.account.dto.AdminMyPageResponse;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.DemoteFromAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.PromoteToAdminRequest;
import Hongik_SafeMap_Server.domain.admin.account.dto.request.UpdateAdminNicknameRequest;
import Hongik_SafeMap_Server.domain.admin.account.service.AdminAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 계정 관리")
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

    @Operation(summary = "관리자 권한 박탈", description = "이메일로 관리자 권한을 박탈하고 일반 사용자로 전환합니다.")
    @PostMapping("/demote")
    public ResponseEntity<Void> demoteFromAdmin(@Valid @RequestBody DemoteFromAdminRequest request) {
        adminAccountService.demoteFromAdmin(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관리자 닉네임 수정", description = "관리자의 닉네임을 수정합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateAdminNickname(@Valid @RequestBody UpdateAdminNicknameRequest request) {
        adminAccountService.updateAdminNickname(request);
        return ResponseEntity.ok().build();
    }
}
