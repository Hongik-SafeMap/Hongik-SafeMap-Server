package Hongik_SafeMap_Server.domain.safety_tip.repository;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyWarning;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafetyWarningRepository extends JpaRepository<SafetyWarning, Long> {
    void deleteBySafetyTip(SafetyTip safetyTip);
}