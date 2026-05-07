package Hongik_SafeMap_Server.domain.terms_policy.controller;

import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyListResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.service.PrivacyPolicyService;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsVersionAgreeRequest;
import Hongik_SafeMap_Server.domain.terms.dto.response.MyAgreementResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsListResponse;
import Hongik_SafeMap_Server.domain.terms.service.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "이용약관 & 개인정보처리방침", description = "약관 동의 및 버전 조회 API")
@RestController
@RequiredArgsConstructor
public class TermsPolicyController {

    private final TermsService termsService;
    private final PrivacyPolicyService privacyPolicyService;

    @Operation(summary = "최신 버전 이용약관 조회", description = "가장 높은 버전의 이용약관 전체 내용을 조회합니다. 등록된 항목이 없으면 204를 반환합니다.")
    @GetMapping("/terms")
    public ResponseEntity<TermsListResponse> getLatestTerms() {
        return termsService.getLatestTerms()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "최신 버전 개인정보처리방침 조회", description = "가장 높은 버전의 개인정보처리방침 전체 내용을 조회합니다. 등록된 항목이 없으면 204를 반환합니다.")
    @GetMapping("/privacy-policy")
    public ResponseEntity<PrivacyPolicyListResponse> getLatestPolicies() {
        return privacyPolicyService.getLatestPolicies()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(summary = "특정 버전 이용약관 및 개인정보처리방침 동의", description = "특정 버전의 이용약관 및 개인정보처리방침에 동의합니다.")
    @PostMapping("/terms/agree")
    public ResponseEntity<Void> agreeToVersion(@Valid @RequestBody TermsVersionAgreeRequest request) {
        termsService.agreeToVersion(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 이용약관 및 개인정보처리방침 동의 현황 조회", description = "로그인한 사용자의 약관과 개인정보처리방침 동의 이력을 조회합니다.")
    @GetMapping("/terms/my-agreements")
    public ResponseEntity<List<MyAgreementResponse>> getMyAgreements() {
        return ResponseEntity.ok(termsService.getMyAgreements());
    }
}
