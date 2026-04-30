package Hongik_SafeMap_Server.config;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final String ICON_FOLDER = "disaster-type-icons";

    private static final Map<String, String> DEFAULT_DISASTER_TYPES = new LinkedHashMap<>() {{
        put("화재", "fire.svg");
        put("지진", "earthquake.svg");
        put("홍수", "flood.svg");
        put("산사태", "landslide.svg");
        put("태풍", "typhoon.svg");
    }};
    private final DisasterTypeRepository disasterTypeRepository;
    private final SafetyTipRepository safetyTipRepository;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDisasterTypes();
        initializeSafetyTips();
    }

    private void initializeDisasterTypes() {
        DEFAULT_DISASTER_TYPES.forEach((name, iconFileName) -> {
            String iconUrl = iconFileName != null
                    ? String.format("https://%s.s3.%s.amazonaws.com/%s/%s", bucketName, region, ICON_FOLDER, iconFileName)
                    : null;

            disasterTypeRepository.findByName(name).ifPresentOrElse(
                    existing -> {
                        if (existing.getIconUrl() == null && iconUrl != null) {
                            existing.updateIconUrl(iconUrl);
                        }
                    },
                    () -> disasterTypeRepository.save(
                            DisasterType.builder().name(name).iconUrl(iconUrl).build()
                    )
            );
        });
        log.info("재난 유형 등록 완료");
    }

    private void initializeSafetyTips() {
        List<DisasterType> disasterTypes = disasterTypeRepository.findAllByOrderByIdAsc();
        disasterTypes.stream()
                .forEach(disasterType -> {
                    if (safetyTipRepository.findByDisasterType(disasterType).isEmpty()) {
                        SafetyTip safetyTip = SafetyTip.builder()
                                .disasterType(disasterType)
                                .title(disasterType.getName() + " 발생 시 행동요령")
                                .detail("")
                                .build();
                        safetyTipRepository.save(safetyTip);
                    }
                });
        log.info("행동 요령 등록 완료");
    }
}
