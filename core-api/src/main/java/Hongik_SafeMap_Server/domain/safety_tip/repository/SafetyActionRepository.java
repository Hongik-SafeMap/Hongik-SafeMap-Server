package Hongik_SafeMap_Server.domain.safety_tip.repository;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyAction;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafetyActionRepository extends JpaRepository<SafetyAction, Long> {

    void deleteBySafetyTip(SafetyTip safetyTip);
}