package Hongik_SafeMap.Hongik_SafeMap_Server.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {

    private final Environment environment;

    public Stream<String> getActiveProfiles() { return Stream.of(environment.getActiveProfiles()); }

    public String getCurrentProfile() {
        return getActiveProfiles()
                .filter(profile -> profile.equalsIgnoreCase("prod") || profile.equalsIgnoreCase("dev"))
                .findFirst()
                .orElse("local");
    }
}
