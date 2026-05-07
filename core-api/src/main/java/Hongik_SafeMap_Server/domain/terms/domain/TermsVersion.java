package Hongik_SafeMap_Server.domain.terms.domain;

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
public class TermsVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_version_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String version;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    private TermsVersion(String version, String title, LocalDate date) {
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
