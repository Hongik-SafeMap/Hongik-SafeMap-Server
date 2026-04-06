package Hongik_SafeMap_Server.domain.safety_tip.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import Hongik_SafeMap_Server.vo.DisasterType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SafetyTip extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safety_tip_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DisasterType disasterType;
    @Column(nullable = false, length = 20)
    private String title;


    @Column(nullable = false, length = 1000)
    private String detail;

    @OneToMany(mappedBy = "safetyTip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SafetyAction> actions = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "safety_tip_supplies", joinColumns = @JoinColumn(name = "safety_tip_id"))
    @Column(name = "supply")
    private Set<String> supplies = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "safety_tip_warnings", joinColumns = @JoinColumn(name = "safety_tip_id"))
    @Column(name = "warning")
    private Set<String> warnings = new HashSet<>();

    @Builder
    private SafetyTip(DisasterType disasterType, String title, String detail) {
        this.disasterType = disasterType;
        this.title = title;
        this.detail = detail;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateSupplies(List<String> supplies) {
        this.supplies.clear();
        if (supplies != null) {
            this.supplies.addAll(supplies);
        }
    }

    public void updateWarnings(List<String> warnings) {
        this.warnings.clear();
        if (warnings != null) {
            this.warnings.addAll(warnings);
        }
    }
}