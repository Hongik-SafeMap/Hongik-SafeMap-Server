package Hongik_SafeMap_Server.domain.notification_preference.repository;

import Hongik_SafeMap_Server.domain.notification_preference.domain.NotificationPreference;
import Hongik_SafeMap_Server.vo.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    List<NotificationPreference> findByMemberId(Long memberId);

    Optional<NotificationPreference> findByMemberIdAndDisasterType(Long memberId, DisasterType disasterType);
}