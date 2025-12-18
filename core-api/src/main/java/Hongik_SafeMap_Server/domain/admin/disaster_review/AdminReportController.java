package Hongik_SafeMap_Server.domain.admin.disaster_review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {
    private final AdminReportService adminReportService;

    // 전체 제보 목록
    @GetMapping
    public ResponseEntity<Page<AdminReportListResponse>> getReports(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adminReportService.findAllReports(pageable));
    }
}
