package Hongik_SafeMap_Server.domain.member.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.member.dto.request.MemberPasswordChangeRequest;
import Hongik_SafeMap_Server.domain.member.dto.response.MyPageResponse;
import Hongik_SafeMap_Server.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 마이페이지
    @GetMapping("/me")
    public ResponseEntity<MyPageResponse> getMyPage() {
        MyPageResponse response = memberService.getMyPage();
        return ResponseEntity.ok().body(response);
    }

    // 내 제보 조회
    @GetMapping("/me/reports")
    public ResponseEntity<Page<DisasterReportListResponse>> getMyReports(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMyReports(pageable));
    }

    // 비밀번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody MemberPasswordChangeRequest request) {
        memberService.updatePassword(request);
        return ResponseEntity.ok().build();
    }
}
