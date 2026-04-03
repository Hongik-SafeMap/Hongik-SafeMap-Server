package Hongik_SafeMap_Server.domain.safety_tip.repository;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetySupply;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafetySupplyRepository extends JpaRepository<SafetySupply, Long> {
    void deleteBySafetyTip(SafetyTip safetyTip);
}