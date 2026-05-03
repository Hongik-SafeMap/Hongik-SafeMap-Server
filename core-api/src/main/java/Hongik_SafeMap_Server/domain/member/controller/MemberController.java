package Hongik_SafeMap_Server.domain.member.controller;

import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportPageResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportsPageResponse;
import Hongik_SafeMap_Server.domain.member.dto.request.MemberPasswordChangeRequest;
import Hongik_SafeMap_Server.domain.member.dto.response.MyPageResponse;
import Hongik_SafeMap_Server.domain.member.service.MemberService;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportsPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<DisasterReportPageResponse> getMyReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(memberService.getMyReports(page, size));
    }

    // 내 자원 게시글 조회
    @GetMapping("/me/resource-reports")
    public ResponseEntity<ResourceReportsPageResponse> getMyResourceReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(memberService.getMyResourceReports(page, size));
    }

    // 내 실종 게시글 조회
    @GetMapping("/me/lost-reports")
    public ResponseEntity<LostReportsPageResponse> getMyLostReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(memberService.getMyLostReports(page, size));
    }

    // 비밀번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody MemberPasswordChangeRequest request) {
        memberService.updatePassword(request);
        return ResponseEntity.ok().build();
    }
}
