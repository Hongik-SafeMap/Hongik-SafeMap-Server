package Hongik_SafeMap_Server.domain.admin.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private static final String KEY = "maintenace_mode";

    private final StringRedisTemplate redisTemplate;

    public boolean isUnderMaintenance() {
        String value = redisTemplate.opsForValue().get(KEY);
        return "true".equals(value);
    }

    public void setMaintenance(boolean status) {
        redisTemplate.opsForValue().set(KEY, String.valueOf(status));
    }
}
