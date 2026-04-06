package Hongik_SafeMap_Server.config;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import Hongik_SafeMap_Server.vo.DisasterType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final SafetyTipRepository safetyTipRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeSafetyTips();
    }

    private void initializeSafetyTips() {

        Arrays.stream(DisasterType.values())
                .forEach(disasterType -> {
                    if (disasterType != DisasterType.ETC && safetyTipRepository.findByDisasterType(disasterType).isEmpty()) {
                        SafetyTip safetyTip = SafetyTip.builder()
                                .disasterType(disasterType)
                                .title(disasterType.getDescription() + " 발생 시 행동요령")
                                .detail("")
                                .build();
                        safetyTipRepository.save(safetyTip);
                    }
                });
        log.info("행동 요령 등록 완료");
    }
}