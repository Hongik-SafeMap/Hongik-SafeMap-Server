package Hongik_SafeMap_Server.domain.disaster_report_group.controller;

import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupedDisasterReportResponse;
import Hongik_SafeMap_Server.domain.disaster_report_group.service.DisasterReportGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disaster-reports")
@Tag(name = "재난 제보 그룹 조회")
public class DisasterReportGroupController {
    private final DisasterReportGroupService disasterReportGroupService;

    @Operation(summary = "재난 제보 클러스터 조회", description = """
            실시간 지도(마지막 업데이트 24시간 이내)에서 클러스터링 된 재난 제보를 조회하기 위해 사용할 수 있습니다.
            isActive 파라미터를 false로 설정하면 전체 재난 제보를 조회할 수 있습니다.
            사용자 위치로부터 반경 몇 미터 내의 재난 제보를 조회할지 설정해서 조회할 수 있습니다.
            사용자 위치 정보를 담지 않으면 클러스터링된 전체 지도 목록를 조회할 수 있습니다.
            재난 제보 유형별, 위치별(500m)로 클러스터링된 그룹입니다.
            """)
    @GetMapping("/grouped")
    public ResponseEntity<List<GroupedDisasterReportResponse>> getGroupedReports(
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude,
            @RequestParam(name = "radiusMeters", defaultValue = "10000") int radiusMeters,
            @RequestParam(name = "isActive", defaultValue = "true", required = false) Boolean isActive) {
        return ResponseEntity.ok(disasterReportGroupService.getGroupedReports(latitude, longitude, radiusMeters, isActive));
    }
}
