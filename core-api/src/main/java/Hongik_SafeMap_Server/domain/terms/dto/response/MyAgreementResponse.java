package Hongik_SafeMap_Server.domain.terms.dto.response;

import Hongik_SafeMap_Server.domain.terms.domain.TermsAgreement;

import java.time.LocalDateTime;

public record MyAgreementResponse(
        String termsVersion,
        String privacyPolicyVersion,
        LocalDateTime agreedAt
) {
    public static MyAgreementResponse of(TermsAgreement agreement) {
        return new MyAgreementResponse(
                agreement.getTermsVersion().getVersion(),
                agreement.getPrivacyPolicyVersion().getVersion(),
                agreement.getCreatedAt()
        );
    }
}
