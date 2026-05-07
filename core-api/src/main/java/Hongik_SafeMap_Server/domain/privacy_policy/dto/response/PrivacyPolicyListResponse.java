package Hongik_SafeMap_Server.domain.privacy_policy.dto.response;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicy;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PrivacyPolicyListResponse(
        String version,
        String title,
        LocalDate date,
        LocalDateTime createdAt,
        List<PrivacyPolicyItem> sections
) {
    public record PrivacyPolicyItem(
            Long id,
            String header,
            String content
    ) {
        public static PrivacyPolicyItem of(PrivacyPolicy policy) {
            return new PrivacyPolicyItem(policy.getId(), policy.getHeader(), policy.getContent());
        }
    }

    public static PrivacyPolicyListResponse of(PrivacyPolicyVersion privacyPolicyVersion, List<PrivacyPolicy> policyList) {
        return new PrivacyPolicyListResponse(
                privacyPolicyVersion.getVersion(),
                privacyPolicyVersion.getTitle(),
                privacyPolicyVersion.getDate(),
                privacyPolicyVersion.getCreatedAt(),
                policyList.stream().map(PrivacyPolicyItem::of).toList()
        );
    }
}
