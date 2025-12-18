package Hongik_SafeMap_Server.domain.auth.dto.request;

import Hongik_SafeMap_Server.global.annotation.ValidEnum;
import Hongik_SafeMap_Server.vo.LoginType;
import jakarta.validation.constraints.NotBlank;


public record SnsLoginRequest(
        @NotBlank(message = "SNS 액세스 토큰은 필수입니다.")
        String token,

        @ValidEnum(enumClass = LoginType.class, message = "로그인 타입(카카오, 네이버 등)은 필수입니다.")
        String loginType
) {}
