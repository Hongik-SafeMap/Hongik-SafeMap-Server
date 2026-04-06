package Hongik_SafeMap_Server.domain.admin.activity.dto.response;

import Hongik_SafeMap_Server.domain.admin.activity.domain.AdminActivityLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminActivityLogResponse {
    private Long id;
    private Long adminId;
    private String description;
    private LocalDateTime createdAt;

    public static AdminActivityLogResponse of(AdminActivityLog log) {
        return AdminActivityLogResponse.builder()
                .id(log.getId())
                .adminId(log.getAdminId())
                .description(log.getDescription())
                .createdAt(log.getCreatedAt())
                .build();
    }
}