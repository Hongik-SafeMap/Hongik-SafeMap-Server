package Hongik_SafeMap_Server.domain.terms.dto.response;

import Hongik_SafeMap_Server.domain.terms.domain.Terms;
import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TermsListResponse(
        String version,
        String title,
        LocalDate date,
        LocalDateTime createdAt,
        List<TermsItem> sections
) {
    public record TermsItem(
            Long id,
            String header,
            String content
    ) {
        public static TermsItem of(Terms terms) {
            return new TermsItem(terms.getId(), terms.getHeader(), terms.getContent());
        }
    }

    public static TermsListResponse of(TermsVersion termsVersion, List<Terms> termsList) {
        return new TermsListResponse(
                termsVersion.getVersion(),
                termsVersion.getTitle(),
                termsVersion.getDate(),
                termsVersion.getCreatedAt(),
                termsList.stream().map(TermsItem::of).toList()
        );
    }
}
