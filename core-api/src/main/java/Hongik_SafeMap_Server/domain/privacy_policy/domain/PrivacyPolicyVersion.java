package Hongik_SafeMap_Server.domain.privacy_policy.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivacyPolicyVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_policy_version_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String version;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    private PrivacyPolicyVersion(String version, String title, LocalDate date) {
        this.version = version;
        this.title = title;
        this.date = date;
    }

    public void update(String version, String title, LocalDate date) {
        this.version = version;
        this.title = title;
        this.date = date;
    }
}
