package Hongik_SafeMap_Server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import Hongik_SafeMap_Server.util.EnvironmentUtil;

import java.util.List;

import static Hongik_SafeMap_Server.constant.EnvironmentConstant.LOCAL_SERVER_URL;

@Configuration
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class SwaggerConfig {

    private final EnvironmentUtil environmentUtil;

    @Bean
    public OpenAPI openAPI() {
        String activeProfile = environmentUtil.getCurrentProfile();

        Server server = new Server();
        if (activeProfile.equalsIgnoreCase("local")) {
            server.setUrl(LOCAL_SERVER_URL);
        }

        return new OpenAPI()
                .servers(List.of(server))
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Hongik SafeMap Server")
                .version("1.0.0");
    }

}
