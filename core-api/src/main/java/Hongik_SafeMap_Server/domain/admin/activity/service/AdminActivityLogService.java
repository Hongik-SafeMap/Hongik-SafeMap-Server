package Hongik_SafeMap_Server.domain.admin.activity.service;

import Hongik_SafeMap_Server.domain.admin.activity.domain.AdminActivityLog;
import Hongik_SafeMap_Server.domain.admin.activity.dto.response.AdminActivityLogResponse;
import Hongik_SafeMap_Server.domain.admin.activity.repository.AdminActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminActivityLogService {

    private final AdminActivityLogRepository adminActivityLogRepository;

    @Async
    @Transactional
    public void saveLog(Long adminId, String description) {
        try {
            AdminActivityLog log = AdminActivityLog.builder()
                    .adminId(adminId)
                    .description(description)
                    .build();

            adminActivityLogRepository.save(log);

        } catch (Exception e) {
            // 로깅 실패가 비즈니스 로직에 영향을 주지 않도록 예외를 잡아서 로그만 남김
            log.error("Failed to save admin activity log: adminId={}, description={}", adminId, description, e);
        }
    }

    public Page<AdminActivityLogResponse> getActivityLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AdminActivityLog> logs = adminActivityLogRepository.findAll(pageable);
        return logs.map(AdminActivityLogResponse::of);
    }
}