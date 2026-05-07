package Hongik_SafeMap_Server.domain.terms.dto.response;

import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;

import java.time.LocalDateTime;

public record TermsVersionResponse(
        String version,
        LocalDateTime updatedAt
) {
    public static TermsVersionResponse of(TermsVersion termsVersion) {
        return new TermsVersionResponse(
                termsVersion.getVersion(),
                termsVersion.getUpdatedAt()
        );
    }
}
