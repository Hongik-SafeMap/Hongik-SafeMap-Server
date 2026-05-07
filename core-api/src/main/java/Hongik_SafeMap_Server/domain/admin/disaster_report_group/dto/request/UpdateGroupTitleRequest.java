package Hongik_SafeMap_Server.domain.admin.disaster_report_group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateGroupTitleRequest(
        @Schema(description = "재난 제보 그룹 제목", example = "홍익대 인근 화재")
        @NotBlank(message = "그룹 제목은 필수 입력값입니다.")
        @Size(max = 100, message = "그룹 제목은 100자 이하로 입력해주세요.")
        String title
) {}
