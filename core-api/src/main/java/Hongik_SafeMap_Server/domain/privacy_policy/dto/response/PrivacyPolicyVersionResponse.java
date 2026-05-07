package Hongik_SafeMap_Server.domain.privacy_policy.dto.response;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;

import java.time.LocalDateTime;

public record PrivacyPolicyVersionResponse(
        String version,
        LocalDateTime updatedAt
) {
    public static PrivacyPolicyVersionResponse of(PrivacyPolicyVersion privacyPolicyVersion) {
        return new PrivacyPolicyVersionResponse(
                privacyPolicyVersion.getVersion(),
                privacyPolicyVersion.getUpdatedAt()
        );
    }
}
