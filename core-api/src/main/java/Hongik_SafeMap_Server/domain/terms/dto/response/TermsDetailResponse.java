package Hongik_SafeMap_Server.domain.terms.dto.response;

import Hongik_SafeMap_Server.domain.terms.domain.Terms;

public record TermsDetailResponse(
        Long id,
        String header,
        String content
) {
    public static TermsDetailResponse of(Terms terms) {
        return new TermsDetailResponse(
                terms.getId(),
                terms.getHeader(),
                terms.getContent()
        );
    }
}
