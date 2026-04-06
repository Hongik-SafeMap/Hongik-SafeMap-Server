package Hongik_SafeMap_Server.domain.safety_tip.repository;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.vo.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SafetyTipRepository extends JpaRepository<SafetyTip, Long> {

    Optional<SafetyTip> findByDisasterType(DisasterType disasterType);

    List<SafetyTip> findAllByOrderByIdAsc();

    @Query("SELECT st FROM SafetyTip st LEFT JOIN FETCH st.actions LEFT JOIN FETCH st.supplies LEFT JOIN FETCH st.warnings WHERE st.disasterType = :disasterType")
    Optional<SafetyTip> findByDisasterTypeWithActions(@Param("disasterType") DisasterType disasterType);

    @Query("SELECT st FROM SafetyTip st LEFT JOIN FETCH st.actions LEFT JOIN FETCH st.supplies LEFT JOIN FETCH st.warnings ORDER BY st.id ASC")
    List<SafetyTip> findAllWithActions();
}