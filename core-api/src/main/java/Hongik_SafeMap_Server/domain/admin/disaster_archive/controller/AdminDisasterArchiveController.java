package Hongik_SafeMap_Server.domain.admin.disaster_archive.controller;

import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.request.UpdateGroupTitleRequest;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterRecordListResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterSimulationResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.GroupLocationResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.service.AdminDisasterArchiveService;
import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupDetailResponse;
import Hongik_SafeMap_Server.vo.RiskLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/disaster-archive")
@Tag(name = "관리자 통계 및 아카이브")
public class AdminDisasterArchiveController {

    private final AdminDisasterArchiveService adminDisasterArchiveService;

    @Operation(summary = "재난 통계 요약",
            description = "재난 유형 ID 목록(disasterTypeIds)과 기간(from, to)으로 필터링 가능. 모두 선택사항.")
    @GetMapping("/statistics")
    public ResponseEntity<DisasterStatisticsSummaryResponse> getStatisticsSummary(
            @RequestParam(name = "disasterTypeIds", required = false) List<Long> disasterTypeIds,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(adminDisasterArchiveService.getStatisticsSummary(disasterTypeIds, from, to));
    }

    @Operation(summary = "재난 제보 그룹 상세 조회", description = "특정 그룹의 통계 정보와 속한 재난 제보 목록을 함께 조회합니다.")
    @GetMapping("/disaster-records/{groupId}")
    public ResponseEntity<GroupDetailResponse> getGroupDetail(
            @Parameter(description = "그룹 ID", example = "1")
            @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(adminDisasterArchiveService.getGroupDetail(groupId));
    }

    @Operation(summary = "재난 제보 그룹 제목 등록/수정", description = "재난 제보 그룹의 제목을 등록하거나 수정합니다.")
    @PatchMapping("/disaster-records/{groupId}/title")
    public ResponseEntity<Void> updateGroupTitle(
            @Parameter(description = "그룹 ID", example = "1")
            @PathVariable("groupId") Long groupId,
            @Valid @RequestBody UpdateGroupTitleRequest request) {
        adminDisasterArchiveService.updateGroupTitle(groupId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "재난 기록 전체 조회", description = "활성/비활성 포함 전체 재난 기록(그룹) 목록을 조회합니다. riskLevels, 기간(from, to)로 필터링 가능.")
    @GetMapping("/disaster-records")
    public ResponseEntity<DisasterRecordListResponse> getDisasterRecords(
            @RequestParam(name = "riskLevels", required = false) List<RiskLevel> riskLevels,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminDisasterArchiveService.getDisasterRecords(riskLevels, from, to, page, size));
    }

    @Operation(summary = "재난 기록 위치 조회", description = "그룹 정보와 속한 각 제보의 위도·경도를 조회합니다.")
    @GetMapping("/disaster-records/{groupId}/locations")
    public ResponseEntity<GroupLocationResponse> getGroupLocation(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(adminDisasterArchiveService.getGroupLocation(groupId));
    }

    @Operation(summary = "재난 시뮬레이션 조회", description = "제보가 들어온 순서대로 위치와 통계 변화를 프레임 단위로 조회합니다.")
    @GetMapping("/disaster-records/{groupId}/simulation")
    public ResponseEntity<DisasterSimulationResponse> getDisasterSimulation(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(adminDisasterArchiveService.getDisasterSimulation(groupId));
    }
}
