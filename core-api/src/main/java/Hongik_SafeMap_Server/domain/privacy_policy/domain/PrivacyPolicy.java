package Hongik_SafeMap_Server.domain.privacy_policy.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivacyPolicy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_policy_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String header;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privacy_policy_version_id", nullable = false)
    private PrivacyPolicyVersion privacyPolicyVersion;

    @Builder
    private PrivacyPolicy(String header, String content, PrivacyPolicyVersion privacyPolicyVersion) {
        this.header = header;
        this.content = content;
        this.privacyPolicyVersion = privacyPolicyVersion;
    }

    public void update(String header, String content) {
        this.header = header;
        this.content = content;
    }
}
