package Hongik_SafeMap_Server.domain.member.controller;

import Hongik_SafeMap_Server.domain.member.dto.request.MemberPasswordChangeRequest;
import Hongik_SafeMap_Server.domain.member.dto.response.MyPageResponse;
import Hongik_SafeMap_Server.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MyPageResponse> getMyPage() {
        MyPageResponse response = memberService.getMyPage();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody MemberPasswordChangeRequest request) {
        memberService.updatePassword(request);
        return ResponseEntity.ok().build();
    }
}
