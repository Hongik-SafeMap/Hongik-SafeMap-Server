package Hongik_SafeMap_Server.domain.admin.disaster_review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {
    private final AdminReportService adminReportService;

    // 전체 제보 목록 (제보 평가 및 신고수 포함)
    @GetMapping
    public ResponseEntity<AdminReportPageResponse> getReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminReportService.findAllReports(page, size));
    }
}
