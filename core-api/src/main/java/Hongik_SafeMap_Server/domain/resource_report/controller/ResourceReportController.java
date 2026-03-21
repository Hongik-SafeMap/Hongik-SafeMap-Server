package Hongik_SafeMap_Server.domain.resource_report.controller;

import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportCommentsResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportResponse;
import Hongik_SafeMap_Server.domain.resource_report.service.ResourceReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "자원 요청/공급", description = "자원 요청/공급 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-board")
public class ResourceReportController {

    private final ResourceReportService resourceReportService;

    @Operation(summary = "자원 요청/공급 등록", description = "자원 요청 또는 공급 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ResourceReportCreateRequest request) {
        Long reportId = resourceReportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    @Operation(summary = "자원 게시글 조회", description = "ID로 자원 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResourceReportResponse> findById(@PathVariable Long id) {
        ResourceReportResponse response = resourceReportService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자원 게시글 댓글 작성", description = "자원 게시글에 댓글을 작성합니다.")
    @PostMapping("/{id}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long id,
                                              @Valid @RequestBody ResourceReportCommentCreateRequest request) {
        Long commentId = resourceReportService.createComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @Operation(summary = "자원 게시글 댓글 목록 조회", description = "자원 게시글의 댓글 목록을 조회합니다.")
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResourceReportCommentsResponse> getCommentsById(@PathVariable Long id) {
        ResourceReportCommentsResponse response = resourceReportService.getComments(id);
        return ResponseEntity.ok(response);
    }
}