package Hongik_SafeMap_Server.domain.member.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static Hongik_SafeMap_Server.constant.RequestFormatConstant.PASSWORD_REGEX;
import static Hongik_SafeMap_Server.exception.ErrorMessage.PASSWORD_INVALID_FORMAT;

public record SignUpRequest(@NotBlank(message = "이름은 필수 입력값입니다.")
                            @Pattern(regexp = "[가-힣]{1,6}$", message = "이름은 한글 6자 이하로 입력해주세요.")
                            String name,

                            @NotBlank(message = "이메일은 필수 입력값입니다.")
                            @Email(message = "이메일 형식이 올바르지 않습니다.")
                            String email,

                            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
                            @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_INVALID_FORMAT)
                            String password,

                            @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
                            String passwordConfirm,

                            @NotBlank(message = "휴대전화는 필수 입력값입니다.")
                            @Pattern(regexp = "[0-9]{1,12}$", message = "전화번호는 11자리 이하로 입력해주세요.")
                            String phone) {
}
