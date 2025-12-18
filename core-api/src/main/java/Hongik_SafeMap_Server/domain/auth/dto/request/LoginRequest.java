package Hongik_SafeMap_Server.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static Hongik_SafeMap_Server.constant.RequestFormatConstant.EMAIL_REGEX;
import static Hongik_SafeMap_Server.constant.RequestFormatConstant.PASSWORD_REGEX;
import static Hongik_SafeMap_Server.exception.ErrorMessage.EMAIL_INVALID_FORMAT;
import static Hongik_SafeMap_Server.exception.ErrorMessage.PASSWORD_INVALID_FORMAT;

public record LoginRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(
                regexp = EMAIL_REGEX,
                message = EMAIL_INVALID_FORMAT
        )
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(
                regexp = PASSWORD_REGEX,
                message = PASSWORD_INVALID_FORMAT
        )
        String password
) {}
