package Hongik_SafeMap_Server.domain.admin.disaster_report_group.controller;

import Hongik_SafeMap_Server.domain.admin.disaster_report_group.dto.request.UpdateGroupTitleRequest;
import Hongik_SafeMap_Server.domain.admin.disaster_report_group.service.AdminDisasterReportGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 재난 제보 그룹 관리", description = "재난 제보 그룹 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/disaster-report/grouped")
public class AdminDisasterReportGroupController {

    private final AdminDisasterReportGroupService adminDisasterReportGroupService;

    @Operation(summary = "재난 제보 그룹 제목 등록/수정", description = "재난 제보 그룹의 제목을 등록하거나 수정합니다.")
    @PatchMapping("/{groupId}/title")
    public ResponseEntity<Void> updateGroupTitle(
            @Parameter(description = "그룹 ID", example = "1")
            @PathVariable(name = "groupId") Long groupId,
            @Valid @RequestBody UpdateGroupTitleRequest request) {
        adminDisasterReportGroupService.updateGroupTitle(groupId, request);
        return ResponseEntity.ok().build();
    }
}
