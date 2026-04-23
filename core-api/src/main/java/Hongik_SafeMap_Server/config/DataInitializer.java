package Hongik_SafeMap_Server.config;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final List<String> DEFAULT_DISASTER_TYPES = List.of(
            "화재", "지진", "홍수", "산사태", "태풍", "기타"
    );

    private final DisasterTypeRepository disasterTypeRepository;
    private final SafetyTipRepository safetyTipRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDisasterTypes();
        initializeSafetyTips();
    }

    private void initializeDisasterTypes() {
        DEFAULT_DISASTER_TYPES.forEach(name -> {
            if (!disasterTypeRepository.existsByName(name)) {
                disasterTypeRepository.save(DisasterType.builder().name(name).build());
            }
        });
        log.info("재난 유형 등록 완료");
    }

    private void initializeSafetyTips() {
        List<DisasterType> disasterTypes = disasterTypeRepository.findAllByOrderByIdAsc();
        disasterTypes.stream()
                .filter(dt -> !dt.getName().equals("기타"))
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
