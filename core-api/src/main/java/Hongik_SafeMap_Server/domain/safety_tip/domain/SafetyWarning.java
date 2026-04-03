package Hongik_SafeMap_Server.domain.safety_tip.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SafetyWarning extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safety_warning_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safety_tip_id", nullable = false)
    private SafetyTip safetyTip;

    @Builder
    private SafetyWarning(String content, SafetyTip safetyTip) {
        this.content = content;
        this.safetyTip = safetyTip;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateSafetyTip(SafetyTip safetyTip) {
        this.safetyTip = safetyTip;
    }
}