package Hongik_SafeMap_Server.domain.safety_tip.service;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipSummaryResponse;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.exception.SafetyTipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyTipService {

    private final SafetyTipRepository safetyTipRepository;

    public SafetyTipResponse getSafetyTipByDisasterTypeId(Long disasterTypeId) {
        return safetyTipRepository.findByDisasterTypeIdWithActions(disasterTypeId)
                .map(SafetyTipResponse::of)
                .orElseThrow(() -> new SafetyTipException(ErrorMessage.SAFETY_TIP_NOT_FOUND));
    }

    public List<SafetyTipResponse> getAllSafetyTips() {
        List<SafetyTip> safetyTips = safetyTipRepository.findAllWithActions();

        return safetyTips.stream()
                .map(SafetyTipResponse::of)
                .toList();
    }

    public List<SafetyTipSummaryResponse> getAllSafetyTipsSummary() {
        List<SafetyTip> safetyTips = safetyTipRepository.findAllByOrderByIdAsc();

        return safetyTips.stream()
                .map(SafetyTipSummaryResponse::of)
                .toList();
    }
}
