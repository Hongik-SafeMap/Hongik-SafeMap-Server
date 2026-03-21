package Hongik_SafeMap_Server.domain.resource_report.controller;

import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportUpdateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportCommentsResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportsPageResponse;
import Hongik_SafeMap_Server.domain.resource_report.service.ResourceReportService;
import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import Hongik_SafeMap_Server.vo.ResourceReportType;
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

@Tag(name = "자원 요청/공급", description = "자원 요청/공급 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-board")
public class ResourceReportController {

    private final ResourceReportService resourceReportService;

    @Operation(summary = "자원 게시글 목록 조회", description = "자원 게시글 목록을 페이징으로 조회합니다. 유형, 카테고리, 상태로 필터링 가능합니다.")
    @GetMapping
    public ResponseEntity<ResourceReportsPageResponse> getResourceReports(
            @RequestParam(required = false) ResourceReportType type,
            @RequestParam(required = false) ResourceReportCategory category,
            @RequestParam(required = false) ResourceReportStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        ResourceReportsPageResponse response;
        
        if (type != null && category != null && status != null) {
            response = resourceReportService.getResourceReportsByTypeAndCategoryAndStatus(type, category, status, pageable);
        } else if (type != null && category != null) {
            response = resourceReportService.getResourceReportsByTypeAndCategory(type, category, pageable);
        } else if (type != null && status != null) {
            response = resourceReportService.getResourceReportsByTypeAndStatus(type, status, pageable);
        } else if (category != null && status != null) {
            response = resourceReportService.getResourceReportsByCategoryAndStatus(category, status, pageable);
        } else if (type != null) {
            response = resourceReportService.getResourceReportsByType(type, pageable);
        } else if (category != null) {
            response = resourceReportService.getResourceReportsByCategory(category, pageable);
        } else if (status != null) {
            response = resourceReportService.getResourceReportsByStatus(status, pageable);
        } else {
            response = resourceReportService.getResourceReports(pageable);
        }
        
        return ResponseEntity.ok(response);
    }

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

    @Operation(summary = "자원 게시글 수정", description = "자원 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ResourceReportResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody ResourceReportUpdateRequest request) {
        ResourceReportResponse response = resourceReportService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자원 게시글 삭제", description = "자원 게시글을 삭제합니다. 작성자만 삭제 가능합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceReportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "자원 게시글 댓글 목록 조회", description = "자원 게시글의 댓글 목록을 조회합니다.")
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResourceReportCommentsResponse> getCommentsById(@PathVariable Long id) {
        ResourceReportCommentsResponse response = resourceReportService.getComments(id);
        return ResponseEntity.ok(response);
    }
}