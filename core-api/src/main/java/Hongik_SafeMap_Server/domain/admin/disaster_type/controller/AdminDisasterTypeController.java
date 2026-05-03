package Hongik_SafeMap_Server.domain.admin.disaster_type.controller;

import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeUpdateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.service.DisasterTypeService;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.service.SafetyTipService;
import Hongik_SafeMap_Server.global.dto.request.PresignedUrlRequest;
import Hongik_SafeMap_Server.global.dto.response.PresignedUrlResponse;
import Hongik_SafeMap_Server.global.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 재난 유형 및 행동요령 관리", description = "재난 유형 및 행동요령 관리 API")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/disaster-types")
public class AdminDisasterTypeController {

    private static final String ICON_FOLDER = "disaster-type-icons";

    private final DisasterTypeService disasterTypeService;
    private final SafetyTipService safetyTipService;
    private final S3Service s3Service;

    @Operation(summary = "전체 재난 유형 조회")
    @GetMapping
    public ResponseEntity<List<DisasterTypeResponse>> getAll() {
        return ResponseEntity.ok(disasterTypeService.getAll());
    }

    @Operation(summary = "재난 유형별 행동 요령 조회")
    @GetMapping("/{disasterTypeId}/safety-tip")
    public ResponseEntity<SafetyTipResponse> getSafetyTip(@PathVariable Long disasterTypeId) {
        return ResponseEntity.ok(safetyTipService.getSafetyTipByDisasterTypeId(disasterTypeId));
    }

    @Operation(summary = "재난 유형 및 행동 요령 등록", description = "새로운 재난 유형과 행동 요령을 함께 등록합니다.")
    @PostMapping
    public ResponseEntity<DisasterTypeResponse> create(@Valid @RequestBody DisasterTypeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disasterTypeService.create(request));
    }

    @Operation(summary = "재난 유형 및 행동 요령 수정", description = "재난 유형 이름, 아이콘, 행동 요령을 수정합니다.")
    @PutMapping("/{disasterTypeId}")
    public ResponseEntity<DisasterTypeResponse> update(
            @PathVariable Long disasterTypeId,
            @Valid @RequestBody DisasterTypeUpdateRequest request) {
        return ResponseEntity.ok(disasterTypeService.update(disasterTypeId, request));
    }

    @Operation(summary = "재난 유형 아이콘 업로드 URL 발급", description = "재난 유형 아이콘 업로드를 위한 S3 Presigned URL을 발급합니다. 반환된 imageUrl을 재난 유형 등록/수정 시 iconUrl로 사용하세요.")
    @PostMapping("/upload-icon-url")
    public ResponseEntity<PresignedUrlResponse> generateIconUploadUrl(
            @Valid @RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(s3Service.generatePresignedUrl(request, ICON_FOLDER));
    }
}
