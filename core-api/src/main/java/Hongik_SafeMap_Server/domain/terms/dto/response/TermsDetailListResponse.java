package Hongik_SafeMap_Server.domain.terms.dto.response;

import Hongik_SafeMap_Server.domain.terms.domain.Terms;
import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TermsDetailListResponse(
        String version,
        String title,
        LocalDate date,
        LocalDateTime createdAt,
        List<TermsDetailItem> sections
) {
    public record TermsDetailItem(
            Long id,
            String header,
            String content
    ) {
        public static TermsDetailItem of(Terms terms) {
            return new TermsDetailItem(
                    terms.getId(),
                    terms.getHeader(),
                    terms.getContent()
            );
        }
    }

    public static TermsDetailListResponse of(TermsVersion termsVersion, List<Terms> termsList) {
        return new TermsDetailListResponse(
                termsVersion.getVersion(),
                termsVersion.getTitle(),
                termsVersion.getDate(),
                termsVersion.getCreatedAt(),
                termsList.stream().map(TermsDetailItem::of).toList()
        );
    }
}
