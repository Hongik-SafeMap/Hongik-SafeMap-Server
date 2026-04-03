package Hongik_SafeMap_Server.domain.safety_tip.service;

import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyAction;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetySupply;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyWarning;
import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyActionRepository;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetySupplyRepository;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyWarningRepository;
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
    private final SafetySupplyRepository safetySupplyRepository;
    private final SafetyWarningRepository safetyWarningRepository;

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

    @Transactional
    public void updateSafetyTipByDisasterType(DisasterType disasterType, SafetyTipUpdateRequest request) {
        SafetyTip safetyTip = safetyTipRepository.findByDisasterType(disasterType)
                .orElseThrow(() -> new SafetyTipException(ErrorMessage.SAFETY_TIP_NOT_FOUND));

        safetyTip.updateTitle(request.title());
        safetyTip.updateDetail(request.detail());

        // 기존 관련 엔티티들 삭제
        safetyActionRepository.deleteBySafetyTip(safetyTip);
        safetySupplyRepository.deleteBySafetyTip(safetyTip);
        safetyWarningRepository.deleteBySafetyTip(safetyTip);

        // 새로운 Actions 생성
        List<SafetyAction> newActions = request.actions().stream()
                .map(actionRequest -> SafetyAction.builder()
                        .title(actionRequest.title())
                        .guide(actionRequest.guide())
                        .safetyTip(safetyTip)
                        .build())
                .toList();

        // 새로운 Supplies 생성
        List<SafetySupply> newSupplies = request.supplies().stream()
                .map(supplyRequest -> SafetySupply.builder()
                        .content(supplyRequest.content())
                        .safetyTip(safetyTip)
                        .build())
                .toList();

        // 새로운 Warnings 생성
        List<SafetyWarning> newWarnings = request.warnings().stream()
                .map(warningRequest -> SafetyWarning.builder()
                        .content(warningRequest.content())
                        .safetyTip(safetyTip)
                        .build())
                .toList();

        // 모든 새로운 엔티티들 저장
        safetyActionRepository.saveAll(newActions);
        safetySupplyRepository.saveAll(newSupplies);
        safetyWarningRepository.saveAll(newWarnings);
    }
}