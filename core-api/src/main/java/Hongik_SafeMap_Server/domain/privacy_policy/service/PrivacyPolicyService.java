package Hongik_SafeMap_Server.domain.privacy_policy.service;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import Hongik_SafeMap_Server.domain.privacy_policy.dto.response.PrivacyPolicyListResponse;
import Hongik_SafeMap_Server.domain.privacy_policy.repository.PrivacyPolicyRepository;
import Hongik_SafeMap_Server.domain.privacy_policy.repository.PrivacyPolicyVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivacyPolicyService {

    private final PrivacyPolicyRepository privacyPolicyRepository;
    private final PrivacyPolicyVersionRepository privacyPolicyVersionRepository;

    private static final Comparator<PrivacyPolicyVersion> VERSION_ORDER = Comparator.comparingInt(
            v -> parseVersionNumber(v.getVersion())
    );

    private static int parseVersionNumber(String version) {
        String[] parts = version.split("\\.");
        return Integer.parseInt(parts[0]) * 1000 + Integer.parseInt(parts[1]);
    }

    public Optional<PrivacyPolicyListResponse> getLatestPolicies() {
        return privacyPolicyVersionRepository.findAll().stream()
                .max(VERSION_ORDER)
                .map(pv -> PrivacyPolicyListResponse.of(pv, privacyPolicyRepository.findAllByPrivacyPolicyVersion(pv)));
    }

    public Optional<String> getLatestVersion() {
        return privacyPolicyVersionRepository.findAll().stream()
                .max(VERSION_ORDER)
                .map(PrivacyPolicyVersion::getVersion);
    }
}
