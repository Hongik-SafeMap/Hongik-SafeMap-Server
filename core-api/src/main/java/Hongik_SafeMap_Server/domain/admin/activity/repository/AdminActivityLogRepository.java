package Hongik_SafeMap_Server.domain.admin.activity.repository;

import Hongik_SafeMap_Server.domain.admin.activity.domain.AdminActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActivityLogRepository extends JpaRepository<AdminActivityLog, Long> {

    Page<AdminActivityLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}