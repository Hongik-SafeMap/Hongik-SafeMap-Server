package Hongik_SafeMap_Server.domain.lost_report.controller;

import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportCommentsResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportsPageResponse;
import Hongik_SafeMap_Server.domain.lost_report.service.LostReportService;
import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "실종 신고", description = "실종 신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/missing-board")
public class LostReportController {

    private final LostReportService lostReportService;

    @Operation(summary = "실종 신고 목록 조회", description = "실종신고 게시물 목록을 페이징으로 조회합니다. 카테고리나 상태로 필터링 가능합니다.")
    @GetMapping
    public ResponseEntity<LostReportsPageResponse> getAllLostReports(
            @RequestParam(required = false) LostReportCategory category,
            @RequestParam(required = false) LostReportStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        LostReportsPageResponse response;
        
        if (category != null && status != null) {
            response = lostReportService.getLostReportsByCategoryAndStatus(category, status, pageable);
        } else if (category != null) {
            response = lostReportService.getLostReportsByCategory(category, pageable);
        } else if (status != null) {
            response = lostReportService.getLostReportsByStatus(status, pageable);
        } else {
            response = lostReportService.getLostReports(pageable);
        }
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "실종 신고 등록", description = "실종자 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody LostReportCreateRequest request) {
        Long reportId = lostReportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    @Operation(summary = "실종 신고 상세 조회", description = "실종신고 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<LostReportResponse> getById(@PathVariable Long id) {
        LostReportResponse response = lostReportService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "실종 신고 댓글 작성", description = "실종신고에 댓글을 작성합니다.")
    @PostMapping("/{id}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long id,
                                              @Valid @RequestBody LostReportCommentCreateRequest request) {
        Long commentId = lostReportService.createComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @Operation(summary = "실종 신고 댓글 목록 조회", description = "실종신고 게시물의 댓글 목록을 조회합니다.")
    @GetMapping("/{id}/comments")
    public ResponseEntity<LostReportCommentsResponse> getCommentsById(@PathVariable Long id) {
        LostReportCommentsResponse response = lostReportService.getComments(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "실종 신고 삭제", description = "실종신고 게시물을 삭제합니다. 작성자만 삭제 가능합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lostReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}