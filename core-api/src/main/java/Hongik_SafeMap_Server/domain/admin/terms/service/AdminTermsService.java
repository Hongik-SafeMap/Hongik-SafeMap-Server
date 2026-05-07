package Hongik_SafeMap_Server.domain.admin.terms.service;

import Hongik_SafeMap_Server.domain.terms.domain.Terms;
import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsCreateRequest;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsUpdateRequest;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsDetailListResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsDetailResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsPageResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsVersionResponse;
import Hongik_SafeMap_Server.domain.terms.repository.TermsRepository;
import Hongik_SafeMap_Server.domain.terms.repository.TermsVersionRepository;
import Hongik_SafeMap_Server.exception.TermsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTermsService {

    private final TermsRepository termsRepository;
    private final TermsVersionRepository termsVersionRepository;

    private static final Comparator<TermsDetailListResponse> VERSION_ORDER =
            Comparator.comparingInt(r -> parseVersionNumber(r.version()));

    private static int parseVersionNumber(String version) {
        String[] parts = version.split("\\.");
        return Integer.parseInt(parts[0]) * 1000 + Integer.parseInt(parts[1]);
    }

    public TermsPageResponse getAllTerms(Pageable pageable) {
        List<TermsDetailListResponse> sorted = termsVersionRepository.findAll().stream()
                .sorted(Comparator.comparingInt((TermsVersion v) -> parseVersionNumber(v.getVersion())).reversed())
                .map(tv -> TermsDetailListResponse.of(tv, termsRepository.findAllByTermsVersion(tv)))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        List<TermsDetailListResponse> pageContent = start >= sorted.size() ? List.of() : sorted.subList(start, end);

        return TermsPageResponse.of(new PageImpl<>(pageContent, pageable, sorted.size()));
    }

    public Optional<TermsVersionResponse> getLatestVersion() {
        return termsVersionRepository.findAll().stream()
                .max(Comparator.comparingInt(v -> parseVersionNumber(v.getVersion())))
                .map(TermsVersionResponse::of);
    }

    public Optional<TermsDetailListResponse> getLatestTerms() {
        return termsVersionRepository.findAll().stream()
                .max(Comparator.comparingInt(v -> parseVersionNumber(v.getVersion())))
                .map(tv -> TermsDetailListResponse.of(tv, termsRepository.findAllByTermsVersion(tv)));
    }

    @Transactional
    public List<TermsDetailResponse> createTerms(TermsCreateRequest request) {
        TermsVersion termsVersion = termsVersionRepository.findByVersion(request.version())
                .orElseGet(() -> termsVersionRepository.save(
                        TermsVersion.builder()
                                .version(request.version())
                                .title(request.title())
                                .date(request.date())
                                .build()
                ));

        if (!termsRepository.findAllByTermsVersion(termsVersion).isEmpty()) {
            throw new TermsException(TERMS_ALREADY_EXISTS);
        }

        List<Terms> termsList = request.sections().stream()
                .map(item -> Terms.builder()
                        .header(item.header())
                        .content(item.content())
                        .termsVersion(termsVersion)
                        .build())
                .toList();

        return termsRepository.saveAll(termsList).stream()
                .map(TermsDetailResponse::of)
                .toList();
    }

    @Transactional
    public List<TermsDetailResponse> updateTerms(TermsUpdateRequest request) {
        TermsVersion termsVersion = termsVersionRepository.findByVersion(request.version())
                .orElseThrow(() -> new TermsException(TERMS_NOT_FOUND));

        termsVersion.update(request.version(), request.title(), request.date());

        List<Terms> existing = termsRepository.findAllByTermsVersion(termsVersion);
        termsRepository.deleteAll(existing);

        List<Terms> newTerms = request.sections().stream()
                .map(item -> Terms.builder()
                        .header(item.header())
                        .content(item.content())
                        .termsVersion(termsVersion)
                        .build())
                .toList();

        return termsRepository.saveAll(newTerms).stream()
                .map(TermsDetailResponse::of)
                .toList();
    }
}
