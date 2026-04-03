package Hongik_SafeMap_Server.domain.admin.activity.service;

import Hongik_SafeMap_Server.domain.admin.activity.domain.AdminActivityLog;
import Hongik_SafeMap_Server.domain.admin.activity.dto.response.AdminActivityLogResponse;
import Hongik_SafeMap_Server.domain.admin.activity.repository.AdminActivityLogRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminActivityLogService {

    private final AdminActivityLogRepository adminActivityLogRepository;
    private final MemberRepository memberRepository;

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
            log.error("Failed to save admin activity log: description={}", adminId, description, e);
        }
    }

    public Page<AdminActivityLogResponse> getActivityLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AdminActivityLog> logs = adminActivityLogRepository.findAll(pageable);
        return convertToResponse(logs);
    }

    private Page<AdminActivityLogResponse> convertToResponse(Page<AdminActivityLog> logs) {
        // 관리자 정보 한 번에 조회하여 N+1 문제 방지
        Set<Long> adminIds = logs.getContent().stream()
                .map(AdminActivityLog::getAdminId)
                .collect(Collectors.toSet());

        Map<Long, Member> adminMap = memberRepository.findAllById(adminIds).stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        return logs.map(AdminActivityLogResponse::of);
    }
}