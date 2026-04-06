package Hongik_SafeMap_Server.domain.safety_tip.service;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyAction;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipSummaryResponse;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyActionRepository;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.exception.SafetyTipException;
import Hongik_SafeMap_Server.vo.DisasterType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyTipService {

    private final SafetyTipRepository safetyTipRepository;
    private final SafetyActionRepository safetyActionRepository;

    public SafetyTipResponse getSafetyTipByDisasterType(DisasterType disasterType) {
        return safetyTipRepository.findByDisasterTypeWithActions(disasterType)
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

    @Transactional
    public void updateSafetyTipByDisasterType(DisasterType disasterType, SafetyTipUpdateRequest request) {
        SafetyTip safetyTip = safetyTipRepository.findByDisasterType(disasterType)
                .orElseThrow(() -> new SafetyTipException(ErrorMessage.SAFETY_TIP_NOT_FOUND));

        safetyTip.updateTitle(request.title());
        safetyTip.updateDetail(request.detail());
        safetyTip.updateSupplies(request.supplies());
        safetyTip.updateWarnings(request.warnings());

        // 기존 Actions 삭제 후 새로 생성
        safetyActionRepository.deleteBySafetyTip(safetyTip);

        List<SafetyAction> newActions = request.actions().stream()
                .map(actionRequest -> SafetyAction.builder()
                        .title(actionRequest.title())
                        .guide(actionRequest.guide())
                        .safetyTip(safetyTip)
                        .build())
                .toList();

        safetyActionRepository.saveAll(newActions);
    }
}