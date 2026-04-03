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

    @OneToMany(mappedBy = "safetyTip", cascade = CascadeType.ALL, orphanRemoval = true)  
    private Set<SafetySupply> supplies = new HashSet<>();

    @OneToMany(mappedBy = "safetyTip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SafetyWarning> warnings = new HashSet<>();

    @Builder
    private SafetyTip(DisasterType disasterType, String title, String detail) {
        this.disasterType = disasterType;
        this.title = title;
        this.detail = detail;
    }

    public void addAction(SafetyAction action) {
        this.actions.add(action);
        action.updateSafetyTip(this);
    }

    public void removeAction(SafetyAction action) {
        this.actions.remove(action);
        action.updateSafetyTip(null);
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
    
    public void addSupply(SafetySupply supply) {
        this.supplies.add(supply);
        supply.updateSafetyTip(this);
    }

    public void removeSupply(SafetySupply supply) {
        this.supplies.remove(supply);
        supply.updateSafetyTip(null);
    }

    public void addWarning(SafetyWarning warning) {
        this.warnings.add(warning);
        warning.updateSafetyTip(this);
    }

    public void removeWarning(SafetyWarning warning) {
        this.warnings.remove(warning);
        warning.updateSafetyTip(null);
    }

    public void updateActions(List<SafetyAction> newActions) {
        this.actions.clear();
        newActions.forEach(this::addAction);
    }
}