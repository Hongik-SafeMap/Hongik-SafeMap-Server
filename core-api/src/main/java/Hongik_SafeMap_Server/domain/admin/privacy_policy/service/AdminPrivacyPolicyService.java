package Hongik_SafeMap_Server.domain.admin.privacy_policy.service;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicy;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.request.PrivacyPolicyCreateRequest;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.request.PrivacyPolicyUpdateRequest;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyDetailListResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyDetailResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyPageResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyVersionResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.repository.PrivacyPolicyRepository;
import Hongik_SafeMap_Server.domain.privacy_policy.repository.PrivacyPolicyVersionRepository;
import Hongik_SafeMap_Server.exception.TermsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPrivacyPolicyService {

    private final PrivacyPolicyRepository privacyPolicyRepository;
    private final PrivacyPolicyVersionRepository privacyPolicyVersionRepository;

    private static final Comparator<PrivacyPolicyDetailListResponse> VERSION_ORDER =
            Comparator.comparingInt(r -> parseVersionNumber(r.version()));

    private static int parseVersionNumber(String version) {
        String[] parts = version.split("\\.");
        return Integer.parseInt(parts[0]) * 1000 + Integer.parseInt(parts[1]);
    }

    public PrivacyPolicyPageResponse getAllPolicies(Pageable pageable) {
        List<PrivacyPolicyDetailListResponse> sorted = privacyPolicyVersionRepository.findAll().stream()
                .sorted(Comparator.comparingInt((PrivacyPolicyVersion v) -> parseVersionNumber(v.getVersion())).reversed())
                .map(pv -> PrivacyPolicyDetailListResponse.of(pv, privacyPolicyRepository.findAllByPrivacyPolicyVersion(pv)))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        List<PrivacyPolicyDetailListResponse> pageContent = start >= sorted.size() ? List.of() : sorted.subList(start, end);

        return PrivacyPolicyPageResponse.of(new PageImpl<>(pageContent, pageable, sorted.size()));
    }

    public Optional<PrivacyPolicyVersionResponse> getLatestVersion() {
        return privacyPolicyVersionRepository.findAll().stream()
                .max(Comparator.comparingInt(v -> parseVersionNumber(v.getVersion())))
                .map(PrivacyPolicyVersionResponse::of);
    }

    public Optional<PrivacyPolicyDetailListResponse> getLatestPolicies() {
        return privacyPolicyVersionRepository.findAll().stream()
                .max(Comparator.comparingInt(v -> parseVersionNumber(v.getVersion())))
                .map(pv -> PrivacyPolicyDetailListResponse.of(pv, privacyPolicyRepository.findAllByPrivacyPolicyVersion(pv)));
    }

    @Transactional
    public List<PrivacyPolicyDetailResponse> createPolicies(PrivacyPolicyCreateRequest request) {
        PrivacyPolicyVersion privacyPolicyVersion = privacyPolicyVersionRepository.findByVersion(request.version())
                .orElseGet(() -> privacyPolicyVersionRepository.save(
                        PrivacyPolicyVersion.builder()
                                .version(request.version())
                                .title(request.title())
                                .date(request.date())
                                .build()
                ));

        if (!privacyPolicyRepository.findAllByPrivacyPolicyVersion(privacyPolicyVersion).isEmpty()) {
            throw new TermsException(PRIVACY_POLICY_ALREADY_EXISTS);
        }

        List<PrivacyPolicy> policyList = request.sections().stream()
                .map(item -> PrivacyPolicy.builder()
                        .header(item.header())
                        .content(item.content())
                        .privacyPolicyVersion(privacyPolicyVersion)
                        .build())
                .toList();

        return privacyPolicyRepository.saveAll(policyList).stream()
                .map(PrivacyPolicyDetailResponse::of)
                .toList();
    }

    @Transactional
    public List<PrivacyPolicyDetailResponse> updatePolicies(PrivacyPolicyUpdateRequest request) {
        PrivacyPolicyVersion privacyPolicyVersion = privacyPolicyVersionRepository.findByVersion(request.version())
                .orElseThrow(() -> new TermsException(PRIVACY_POLICY_NOT_FOUND));

        privacyPolicyVersion.update(request.version(), request.title(), request.date());

        List<PrivacyPolicy> existing = privacyPolicyRepository.findAllByPrivacyPolicyVersion(privacyPolicyVersion);
        privacyPolicyRepository.deleteAll(existing);

        List<PrivacyPolicy> newPolicies = request.sections().stream()
                .map(item -> PrivacyPolicy.builder()
                        .header(item.header())
                        .content(item.content())
                        .privacyPolicyVersion(privacyPolicyVersion)
                        .build())
                .toList();

        return privacyPolicyRepository.saveAll(newPolicies).stream()
                .map(PrivacyPolicyDetailResponse::of)
                .toList();
    }
}
