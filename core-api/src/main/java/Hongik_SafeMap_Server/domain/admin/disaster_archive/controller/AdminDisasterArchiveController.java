package Hongik_SafeMap_Server.domain.admin.disaster_archive.controller;

import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterRecordListResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.DisasterStatisticsSummaryResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response.GroupLocationResponse;
import Hongik_SafeMap_Server.domain.admin.disaster_archive.service.AdminDisasterArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "재난 기록 전체 조회", description = "활성/비활성 포함 전체 재난 기록(그룹) 목록을 조회합니다.")
    @GetMapping("/disaster-records")
    public ResponseEntity<DisasterRecordListResponse> getDisasterRecords() {
        return ResponseEntity.ok(adminDisasterArchiveService.getDisasterRecords());
    }

    @Operation(summary = "재난 기록 위치 조회", description = "그룹 정보와 속한 각 제보의 위도·경도를 조회합니다.")
    @GetMapping("/disaster-records/{groupId}/locations")
    public ResponseEntity<GroupLocationResponse> getGroupLocation(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(adminDisasterArchiveService.getGroupLocation(groupId));
    }
}
