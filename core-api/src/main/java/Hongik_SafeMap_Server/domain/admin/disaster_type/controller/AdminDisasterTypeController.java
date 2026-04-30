package Hongik_SafeMap_Server.domain.admin.disaster_type.controller;

import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeUpdateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.service.DisasterTypeService;
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

@Tag(name = "관리자 재난 유형 관리", description = "재난 유형 관리 API")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/disaster-types")
public class AdminDisasterTypeController {

    private static final String ICON_FOLDER = "disaster-type-icons";

    private final DisasterTypeService disasterTypeService;
    private final S3Service s3Service;

    @Operation(summary = "재난 유형 등록", description = "새로운 재난 유형을 등록합니다.")
    @PostMapping
    public ResponseEntity<DisasterTypeResponse> create(@Valid @RequestBody DisasterTypeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disasterTypeService.create(request));
    }

    @Operation(summary = "재난 유형 수정", description = "재난 유형 이름 및 아이콘을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<DisasterTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DisasterTypeUpdateRequest request) {
        return ResponseEntity.ok(disasterTypeService.update(id, request));
    }

    @Operation(summary = "재난 유형 아이콘 업로드 URL 발급", description = "재난 유형 아이콘 업로드를 위한 S3 Presigned URL을 발급합니다. 반환된 imageUrl을 재난 유형 등록/수정 시 iconUrl로 사용하세요.")
    @PostMapping("/upload-icon-url")
    public ResponseEntity<PresignedUrlResponse> generateIconUploadUrl(
            @Valid @RequestBody PresignedUrlRequest request) {
        return ResponseEntity.ok(s3Service.generatePresignedUrl(request, ICON_FOLDER));
    }
}
