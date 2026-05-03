package Hongik_SafeMap_Server.domain.disaster_type.service;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeUpdateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyAction;
import Hongik_SafeMap_Server.domain.safety_tip.domain.SafetyTip;
import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyActionRepository;
import Hongik_SafeMap_Server.domain.safety_tip.repository.SafetyTipRepository;
import Hongik_SafeMap_Server.exception.DisasterTypeException;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisasterTypeService {

    private final DisasterTypeRepository disasterTypeRepository;
    private final SafetyTipRepository safetyTipRepository;
    private final SafetyActionRepository safetyActionRepository;
    private final S3Service s3Service;

    public List<DisasterTypeResponse> getAll() {
        return disasterTypeRepository.findAllByOrderByIdAsc().stream()
                .map(DisasterTypeResponse::of)
                .toList();
    }

    public DisasterType getEntityById(Long id) {
        return disasterTypeRepository.findById(id)
                .orElseThrow(() -> new DisasterTypeException(ErrorMessage.DISASTER_TYPE_NOT_FOUND));
    }

    @Transactional
    public DisasterTypeResponse create(DisasterTypeCreateRequest request) {
        if (disasterTypeRepository.existsByName(request.name())) {
            throw new DisasterTypeException(ErrorMessage.DISASTER_TYPE_ALREADY_EXISTS);
        }
        DisasterType disasterType = DisasterType.builder()
                .name(request.name())
                .iconUrl(request.iconUrl())
                .build();
        disasterTypeRepository.save(disasterType);

        saveSafetyTip(disasterType, request.safetyTip());

        return DisasterTypeResponse.of(disasterType);
    }

    @Transactional
    public DisasterTypeResponse update(Long id, DisasterTypeUpdateRequest request) {
        DisasterType disasterType = disasterTypeRepository.findById(id)
                .orElseThrow(() -> new DisasterTypeException(ErrorMessage.DISASTER_TYPE_NOT_FOUND));
        String oldIconUrl = disasterType.getIconUrl();
        String newIconUrl = request.iconUrl();

        if (oldIconUrl != null && !Objects.equals(oldIconUrl, newIconUrl)) {
            s3Service.deleteFile(oldIconUrl);
        }

        disasterType.updateName(request.name());
        disasterType.updateIconUrl(newIconUrl);

        upsertSafetyTip(disasterType, request.safetyTip());

        return DisasterTypeResponse.of(disasterType);
    }

    private void saveSafetyTip(DisasterType disasterType, SafetyTipUpdateRequest request) {
        SafetyTip safetyTip = SafetyTip.builder()
                .disasterType(disasterType)
                .title(request.title())
                .detail(request.detail())
                .build();
        safetyTip.updateSupplies(request.supplies());
        safetyTip.updateWarnings(request.warnings());
        safetyTipRepository.save(safetyTip);

        List<SafetyAction> actions = request.actions().stream()
                .map(a -> SafetyAction.builder()
                        .title(a.title())
                        .guide(a.guide())
                        .safetyTip(safetyTip)
                        .build())
                .toList();
        safetyActionRepository.saveAll(actions);
    }

    private void upsertSafetyTip(DisasterType disasterType, SafetyTipUpdateRequest request) {
        SafetyTip safetyTip = safetyTipRepository.findByDisasterType(disasterType)
                .orElseGet(() -> safetyTipRepository.save(SafetyTip.builder()
                        .disasterType(disasterType)
                        .title(request.title())
                        .detail(request.detail())
                        .build()));

        safetyTip.updateTitle(request.title());
        safetyTip.updateDetail(request.detail());
        safetyTip.updateSupplies(request.supplies());
        safetyTip.updateWarnings(request.warnings());

        safetyActionRepository.deleteBySafetyTip(safetyTip);
        List<SafetyAction> actions = request.actions().stream()
                .map(a -> SafetyAction.builder()
                        .title(a.title())
                        .guide(a.guide())
                        .safetyTip(safetyTip)
                        .build())
                .toList();
        safetyActionRepository.saveAll(actions);
    }
}
