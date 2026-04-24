package Hongik_SafeMap_Server.domain.disaster_type.controller;

import Hongik_SafeMap_Server.domain.disaster_type.dto.response.DisasterTypeResponse;
import Hongik_SafeMap_Server.domain.disaster_type.service.DisasterTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "재난 유형", description = "재난 유형 조회 API")
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
}
