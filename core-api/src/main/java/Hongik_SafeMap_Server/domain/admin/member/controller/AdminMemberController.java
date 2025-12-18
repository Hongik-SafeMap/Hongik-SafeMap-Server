package Hongik_SafeMap_Server.domain.admin.member.controller;

import Hongik_SafeMap_Server.domain.admin.member.dto.AdminMemberResponse;
import Hongik_SafeMap_Server.domain.admin.member.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    // 사용자 관리 - 회원 리스트 조회
    @GetMapping
    public ResponseEntity<List<AdminMemberResponse>> getMembers() {
        return ResponseEntity.ok(adminMemberService.findAllMembers());
    }

    // 공신력 부여/해제
    @PatchMapping("/{memberId}/credible")
    public ResponseEntity<Void> toggleCredible(@PathVariable Long memberId) {
        adminMemberService.toggleCredible(memberId);
        return ResponseEntity.ok().build();
    }
}
