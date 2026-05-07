package Hongik_SafeMap_Server.domain.terms.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import Hongik_SafeMap_Server.domain.privacy_policy.repository.PrivacyPolicyVersionRepository;
import Hongik_SafeMap_Server.domain.terms.domain.TermsAgreement;
import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;
import Hongik_SafeMap_Server.domain.terms.dto.request.TermsVersionAgreeRequest;
import Hongik_SafeMap_Server.domain.terms.dto.response.MyAgreementResponse;
import Hongik_SafeMap_Server.domain.terms.dto.response.TermsListResponse;
import Hongik_SafeMap_Server.domain.terms.repository.TermsAgreementRepository;
import Hongik_SafeMap_Server.domain.terms.repository.TermsRepository;
import Hongik_SafeMap_Server.domain.terms.repository.TermsVersionRepository;
import Hongik_SafeMap_Server.exception.TermsException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermsService {

    private final TermsRepository termsRepository;
    private final TermsVersionRepository termsVersionRepository;
    private final PrivacyPolicyVersionRepository privacyPolicyVersionRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final MemberUtil memberUtil;

    private static final Comparator<TermsVersion> VERSION_ORDER = Comparator.comparingInt(
            v -> parseVersionNumber(v.getVersion())
    );

    private static int parseVersionNumber(String version) {
        String[] parts = version.split("\\.");
        return Integer.parseInt(parts[0]) * 1000 + Integer.parseInt(parts[1]);
    }

    public Optional<TermsListResponse> getLatestTerms() {
        return termsVersionRepository.findAll().stream()
                .max(VERSION_ORDER)
                .map(tv -> TermsListResponse.of(tv, termsRepository.findAllByTermsVersion(tv)));
    }

    public Optional<String> getLatestVersion() {
        return termsVersionRepository.findAll().stream()
                .max(VERSION_ORDER)
                .map(TermsVersion::getVersion);
    }

    @Transactional
    public void agreeToVersion(TermsVersionAgreeRequest request) {
        TermsVersion termsVersion = termsVersionRepository.findByVersion(request.termsVersion())
                .orElseThrow(() -> new TermsException(TERMS_NOT_FOUND));

        PrivacyPolicyVersion privacyPolicyVersion = privacyPolicyVersionRepository.findByVersion(request.privacyPolicyVersion())
                .orElseThrow(() -> new TermsException(PRIVACY_POLICY_NOT_FOUND));

        Member member = memberUtil.getLoggedInMember();

        termsAgreementRepository.findByMemberIdAndTermsVersionId(member.getId(), termsVersion.getId())
                .orElseGet(() -> termsAgreementRepository.save(
                        TermsAgreement.builder()
                                .member(member)
                                .termsVersion(termsVersion)
                                .privacyPolicyVersion(privacyPolicyVersion)
                                .build()
                ));
    }

    public List<MyAgreementResponse> getMyAgreements() {
        Member member = memberUtil.getLoggedInMember();
        return termsAgreementRepository.findAllByMemberIdWithVersion(member.getId())
                .stream()
                .map(MyAgreementResponse::of)
                .toList();
    }
}
