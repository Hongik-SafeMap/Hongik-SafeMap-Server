package Hongik_SafeMap_Server.domain.admin.privacy_policy.controller;

import Hongik_SafeMap_Server.domain.admin.privacy_policy.service.AdminPrivacyPolicyService;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.request.PrivacyPolicyCreateRequest;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.request.PrivacyPolicyUpdateRequest;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyDetailListResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyDetailResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyPageResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyVersionResponse;
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

@Tag(name = "관리자 개인정보처리방침", description = "개인정보처리방침 등록/수정 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/privacy-policy")
public class AdminPrivacyPolicyController {

    private final AdminPrivacyPolicyService adminPrivacyPolicyService;

    @Operation(summary = "전체 개인정보처리방침 조회", description = "전체 개인정보처리방침을 버전별로 페이징 조회합니다.")
    @GetMapping
    public ResponseEntity<PrivacyPolicyPageResponse> getAllPolicies(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(adminPrivacyPolicyService.getAllPolicies(pageable));
    }

    @Operation(summary = "최신 버전 조회", description = "개인정보처리방침 최신 버전과 수정 날짜를 조회합니다.")
    @GetMapping("/versions/latest")
    public ResponseEntity<PrivacyPolicyVersionResponse> getLatestVersion() {
        return adminPrivacyPolicyService.getLatestVersion()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "최신 버전 개인정보처리방침 조회", description = "가장 높은 버전의 개인정보처리방침 목록을 조회합니다.")
    @GetMapping("/latest")
    public ResponseEntity<PrivacyPolicyDetailListResponse> getLatestPolicies() {
        return adminPrivacyPolicyService.getLatestPolicies()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "개인정보처리방침 등록", description = "여러 항목을 하나의 버전으로 등록합니다. 해당 버전의 개인정보처리방침이 이미 존재하면 등록이 불가합니다.")
    @PostMapping
    public ResponseEntity<List<PrivacyPolicyDetailResponse>> createPolicies(@Valid @RequestBody PrivacyPolicyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminPrivacyPolicyService.createPolicies(request));
    }

    @Operation(summary = "개인정보처리방침 수정", description = "여러 항목을 한번에 수정합니다.")
    @PutMapping
    public ResponseEntity<List<PrivacyPolicyDetailResponse>> updatePolicies(@Valid @RequestBody PrivacyPolicyUpdateRequest request) {
        return ResponseEntity.ok(adminPrivacyPolicyService.updatePolicies(request));
    }
}
