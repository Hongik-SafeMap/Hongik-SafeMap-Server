package Hongik_SafeMap_Server.domain.admin.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAdminNicknameRequest {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Positive(message = "회원 ID는 양수여야 합니다.")
    private Long memberId;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 10, message = "닉네임은 10자를 초과할 수 없습니다.")
    private String nickname;
}