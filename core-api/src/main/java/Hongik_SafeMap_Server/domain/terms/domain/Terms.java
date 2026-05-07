package Hongik_SafeMap_Server.domain.terms.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String header;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_version_id", nullable = false)
    private TermsVersion termsVersion;

    @Builder
    private Terms(String header, String content, TermsVersion termsVersion) {
        this.header = header;
        this.content = content;
        this.termsVersion = termsVersion;
    }

    public void update(String header, String content) {
        this.header = header;
        this.content = content;
    }
}
