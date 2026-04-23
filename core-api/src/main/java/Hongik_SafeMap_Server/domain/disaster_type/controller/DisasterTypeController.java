package Hongik_SafeMap_Server.domain.disaster_type.controller;

import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeCreateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.request.DisasterTypeUpdateRequest;
import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.service.DisasterTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "재난 유형", description = "재난 유형 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/disaster-types")
public class DisasterTypeController {

    private final DisasterTypeService disasterTypeService;

    @Operation(summary = "재난 유형 전체 조회", description = "등록된 재난 유형 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<DisasterTypeResponse>> getAll() {
        return ResponseEntity.ok(disasterTypeService.getAll());
    }

    @Operation(summary = "재난 유형 등록", description = "새로운 재난 유형을 등록합니다. (관리자)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DisasterTypeResponse> create(@Valid @RequestBody DisasterTypeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disasterTypeService.create(request));
    }

    @Operation(summary = "재난 유형 수정", description = "재난 유형 이름을 수정합니다. (관리자)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DisasterTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DisasterTypeUpdateRequest request) {
        return ResponseEntity.ok(disasterTypeService.update(id, request));
    }
}
