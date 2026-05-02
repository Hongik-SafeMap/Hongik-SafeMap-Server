package Hongik_SafeMap_Server.domain.notification.repository;

import Hongik_SafeMap_Server.domain.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    long countByMemberIdAndIsReadFalse(Long memberId);

    List<Notification> findNotificationByMemberIdAndIsReadFalse(Long memberId);
}