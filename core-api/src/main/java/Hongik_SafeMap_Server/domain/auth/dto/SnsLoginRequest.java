package Hongik_SafeMap_Server.domain.auth.dto;

import Hongik_SafeMap_Server.vo.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SnsLoginRequest(
        @NotBlank(message = "SNS 액세스 토큰은 필수입니다.")
        String token,

        @NotNull(message = "로그인 타입(카카오, 네이버 등)은 필수입니다.")
        LoginType loginType
) {}
