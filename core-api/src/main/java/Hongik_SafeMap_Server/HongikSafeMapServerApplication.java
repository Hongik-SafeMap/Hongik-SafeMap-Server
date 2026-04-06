package Hongik_SafeMap_Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAspectJAutoProxy
public class HongikSafeMapServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HongikSafeMapServerApplication.class, args);
	}

}
