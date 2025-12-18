package Hongik_SafeMap_Server.domain.member.controller;

import Hongik_SafeMap_Server.domain.member.dto.request.SignUpRequest;
import Hongik_SafeMap_Server.domain.member.dto.response.SignUpResponse;
import Hongik_SafeMap_Server.domain.member.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<SignUpResponse> registerMember(@Valid @RequestBody SignUpRequest signUpRequest) {
        // 멤버 엔티티 빌드
        SignUpResponse signUpResponse = signUpService.registerMember(signUpRequest);
        return ResponseEntity.ok(signUpResponse);
    }
}
