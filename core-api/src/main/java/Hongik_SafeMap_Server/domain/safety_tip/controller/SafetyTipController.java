package Hongik_SafeMap_Server.domain.safety_tip.controller;

import Hongik_SafeMap_Server.domain.safety_tip.dto.request.SafetyTipUpdateRequest;
import Hongik_SafeMap_Server.domain.safety_tip.dto.response.SafetyTipResponse;
import Hongik_SafeMap_Server.domain.safety_tip.service.SafetyTipService;
import Hongik_SafeMap_Server.vo.DisasterType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/safety-tips")
public class SafetyTipController {

    private final SafetyTipService safetyTipService;

    @GetMapping("/disaster-type")
    public ResponseEntity<SafetyTipResponse> getSafetyTipByDisasterType(@RequestParam(name = "disasterType") DisasterType disasterType) {
        SafetyTipResponse response = safetyTipService.getSafetyTipByDisasterType(disasterType);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SafetyTipResponse>> getAllSafetyTips() {
        List<SafetyTipResponse> responses = safetyTipService.getAllSafetyTips();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/disaster-type")
    public ResponseEntity<Void> updateSafetyTip(@RequestParam(name = "disasterType") DisasterType disasterType, @Valid @RequestBody SafetyTipUpdateRequest request) {
        safetyTipService.updateSafetyTipByDisasterType(disasterType, request);
        return ResponseEntity.ok().build();
    }
}