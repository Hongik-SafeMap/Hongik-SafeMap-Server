package Hongik_SafeMap_Server.domain.privacy_policy.dto.response;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicy;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PrivacyPolicyDetailListResponse(
        String version,
        String title,
        LocalDate date,
        LocalDateTime createdAt,
        List<PrivacyPolicyDetailItem> sections
) {
    public record PrivacyPolicyDetailItem(
            Long id,
            String header,
            String content
    ) {
        public static PrivacyPolicyDetailItem of(PrivacyPolicy policy) {
            return new PrivacyPolicyDetailItem(
                    policy.getId(),
                    policy.getHeader(),
                    policy.getContent()
            );
        }
    }

    public static PrivacyPolicyDetailListResponse of(PrivacyPolicyVersion privacyPolicyVersion, List<PrivacyPolicy> policyList) {
        return new PrivacyPolicyDetailListResponse(
                privacyPolicyVersion.getVersion(),
                privacyPolicyVersion.getTitle(),
                privacyPolicyVersion.getDate(),
                privacyPolicyVersion.getCreatedAt(),
                policyList.stream().map(PrivacyPolicyDetailItem::of).toList()
        );
    }
}
