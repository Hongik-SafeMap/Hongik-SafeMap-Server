package Hongik_SafeMap_Server.domain.admin.terms.controller;

import Hongik_SafeMap_Server.domain.admin.terms.service.AdminTermsService;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsCreateRequest;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsUpdateRequest;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsDetailListResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsDetailResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsPageResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsVersionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 이용약관", description = "이용약관 등록/수정 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/terms")
public class AdminTermsController {

    private final AdminTermsService adminTermsService;

    @Operation(summary = "전체 이용약관 조회", description = "전체 이용약관을 버전별로 페이징 조회합니다.")
    @GetMapping
    public ResponseEntity<TermsPageResponse> getAllTerms(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(adminTermsService.getAllTerms(pageable));
    }

    @Operation(summary = "최신 버전 조회", description = "이용약관 최신 버전과 수정 날짜를 조회합니다.")
    @GetMapping("/versions/latest")
    public ResponseEntity<TermsVersionResponse> getLatestVersion() {
        return adminTermsService.getLatestVersion()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "최신 버전 이용약관 조회", description = "가장 높은 버전의 이용약관 목록을 조회합니다.")
    @GetMapping("/latest")
    public ResponseEntity<TermsDetailListResponse> getLatestTerms() {
        return adminTermsService.getLatestTerms()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "이용약관 등록", description = "여러 항목을 하나의 버전으로 등록합니다. 해당 버전의 이용약관이 이미 존재하면 등록이 불가합니다.")
    @PostMapping
    public ResponseEntity<List<TermsDetailResponse>> createTerms(@Valid @RequestBody TermsCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTermsService.createTerms(request));
    }

    @Operation(summary = "이용약관 수정", description = "이용약관을 수정합니다.")
    @PutMapping
    public ResponseEntity<List<TermsDetailResponse>> updateTerms(@Valid @RequestBody TermsUpdateRequest request) {
        return ResponseEntity.ok(adminTermsService.updateTerms(request));
    }
}
