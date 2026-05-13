package Hongik_SafeMap_Server;

import Hongik_SafeMap_Server.domain.admin.system.service.MaintenanceService;
import Hongik_SafeMap_Server.domain.auth.repository.RefreshTokenRepository;
import Hongik_SafeMap_Server.global.filter.LoginRateLimitFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class HongikSafeMapServerApplicationTests {

	@MockitoBean
	RefreshTokenRepository refreshTokenRepository;

	@MockitoBean
	StringRedisTemplate stringRedisTemplate;

	@MockitoBean
	LoginRateLimitFilter loginRateLimitFilter;

	@MockitoBean
	MaintenanceService maintenanceService;

	@Test
	void contextLoads() {
	}

}
