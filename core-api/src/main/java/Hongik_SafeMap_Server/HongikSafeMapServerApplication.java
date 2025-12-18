package Hongik_SafeMap_Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HongikSafeMapServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HongikSafeMapServerApplication.class, args);
	}

}
