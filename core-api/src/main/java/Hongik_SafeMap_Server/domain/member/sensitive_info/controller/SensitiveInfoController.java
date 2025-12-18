package Hongik_SafeMap_Server.domain.member.sensitive_info.controller;

import Hongik_SafeMap_Server.domain.member.sensitive_info.dto.SensitiveInfoRequest;
import Hongik_SafeMap_Server.domain.member.sensitive_info.dto.SensitiveInfoResponse;
import Hongik_SafeMap_Server.domain.member.sensitive_info.service.SensitiveInfoService;
import Hongik_SafeMap_Server.util.TokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/me/sensitive-info")
public class SensitiveInfoController {
    private final SensitiveInfoService sensitiveInfoService;

    @GetMapping
    public ResponseEntity<SensitiveInfoResponse> getMySensitiveInfo() {
        return ResponseEntity.ok(sensitiveInfoService.getMySensitiveInfo());
    }

    @PutMapping
    public ResponseEntity<SensitiveInfoResponse> upsertMySensitiveInfo(@Valid @RequestBody SensitiveInfoRequest requestBody) {
        return ResponseEntity.ok(sensitiveInfoService.upsertMySensitiveInfo(requestBody));
    }
}
