package Hongik_SafeMap_Server.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static Hongik_SafeMap_Server.constant.RequestFormatConstant.PASSWORD_REGEX;
import static Hongik_SafeMap_Server.exception.ErrorMessage.PASSWORD_INVALID_FORMAT;

public record MemberPasswordChangeRequest(
        @NotBlank(message = "기존 비밀번호를 입력해주세요.")
        String currentPassword,
        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_INVALID_FORMAT)
        String newPassword
) {
}
