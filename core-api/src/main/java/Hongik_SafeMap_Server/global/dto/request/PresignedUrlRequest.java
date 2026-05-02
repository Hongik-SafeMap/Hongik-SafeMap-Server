package Hongik_SafeMap_Server.global.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlRequest {

    @NotBlank(message = "파일명은 필수입니다.")
    @Schema(example = "fire.svg")
    private String fileName;

    @NotBlank(message = "파일 타입은 필수입니다.")
    @Pattern(regexp = "^(image/(jpeg|jpg|png|gif|webp|svg\\+xml))$",
            message = "이미지만 업로드 가능합니다. (이미지: jpeg, jpg, png, gif, webp, svg")
    @Schema(example = "image/svg+xml")
    private String contentType;
}
