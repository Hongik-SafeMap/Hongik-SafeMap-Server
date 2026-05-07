package Hongik_SafeMap_Server.domain.privacy_policy.dto.response;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicy;

public record PrivacyPolicyDetailResponse(
        Long id,
        String header,
        String content
) {
    public static PrivacyPolicyDetailResponse of(PrivacyPolicy policy) {
        return new PrivacyPolicyDetailResponse(
                policy.getId(),
                policy.getHeader(),
                policy.getContent()
        );
    }
}
