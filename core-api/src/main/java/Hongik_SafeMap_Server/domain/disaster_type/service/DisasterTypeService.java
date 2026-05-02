package Hongik_SafeMap_Server.domain.disaster_type.service;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeUpdateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.repository.DisasterTypeRepository;
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
        return DisasterTypeResponse.of(disasterTypeRepository.save(disasterType));
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
        return DisasterTypeResponse.of(disasterType);
    }
}
