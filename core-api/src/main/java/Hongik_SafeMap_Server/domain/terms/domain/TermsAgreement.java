package Hongik_SafeMap_Server.domain.terms.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "terms_agreement",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "terms_version_id"}))
public class TermsAgreement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_agreement_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_version_id", nullable = false)
    private TermsVersion termsVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privacy_policy_version_id", nullable = false)
    private PrivacyPolicyVersion privacyPolicyVersion;

    @Builder
    private TermsAgreement(Member member, TermsVersion termsVersion, PrivacyPolicyVersion privacyPolicyVersion) {
        this.member = member;
        this.termsVersion = termsVersion;
        this.privacyPolicyVersion = privacyPolicyVersion;
    }
}
