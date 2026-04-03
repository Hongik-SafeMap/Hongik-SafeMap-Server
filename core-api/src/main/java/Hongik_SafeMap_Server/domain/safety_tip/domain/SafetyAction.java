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
public class SafetyAction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safety_action_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safety_tip_id", nullable = false)
    private SafetyTip safetyTip;

    @Builder
    private SafetyAction(String title, String guide, SafetyTip safetyTip) {
        this.title = title;
        this.guide = guide;
        this.safetyTip = safetyTip;
    }

    public void updateSafetyTip(SafetyTip safetyTip) {
        this.safetyTip = safetyTip;
    }
}