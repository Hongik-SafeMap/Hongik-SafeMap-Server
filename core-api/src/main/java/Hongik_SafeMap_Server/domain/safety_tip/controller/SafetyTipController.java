package Hongik_SafeMap_Server.domain.safety_tip.controller;

import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipSummaryResponse;
import Hongik_SafeMap_Server.domain.safety_tip.service.SafetyTipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "행동 요령", description = "행동 요령 콘텐츠 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/safety-tips")
public class SafetyTipController {

    private final SafetyTipService safetyTipService;

    @Operation(summary = "재난 유형별 행동 요령 조회", description = "특정 재난 유형 ID에 해당하는 행동 요령을 조회합니다.")
    @GetMapping("/disaster-type")
    public ResponseEntity<SafetyTipResponse> getSafetyTipByDisasterType(
            @Parameter(description = "재난 유형 ID", example = "1")
            @RequestParam(name = "disasterTypeId") Long disasterTypeId) {
        SafetyTipResponse response = safetyTipService.getSafetyTipByDisasterTypeId(disasterTypeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 행동 요령 조회", description = "모든 재난 유형의 행동 요령을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SafetyTipResponse>> getAllSafetyTips() {
        List<SafetyTipResponse> responses = safetyTipService.getAllSafetyTips();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "전체 행동 요령 요약 조회", description = "모든 재난 유형의 행동 요령 요약 정보(id, 재난유형, 제목, 상세)를 조회합니다.")
    @GetMapping("/summary")
    public ResponseEntity<List<SafetyTipSummaryResponse>> getAllSafetyTipsSummary() {
        List<SafetyTipSummaryResponse> responses = safetyTipService.getAllSafetyTipsSummary();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "재난 유형별 행동 요령 수정", description = "특정 재난 유형 ID의 행동 요령을 수정합니다. (관리자)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disaster-type")
    public ResponseEntity<Void> updateSafetyTip(
            @Parameter(description = "재난 유형 ID", example = "1")
            @RequestParam(name = "disasterTypeId") Long disasterTypeId,
            @Valid @RequestBody SafetyTipUpdateRequest request) {
        safetyTipService.updateSafetyTipByDisasterTypeId(disasterTypeId, request);
        return ResponseEntity.ok().build();
    }
}
