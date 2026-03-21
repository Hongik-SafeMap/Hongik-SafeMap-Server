package Hongik_SafeMap_Server.domain.resource_report.dto.request;

import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "자원 요청/공급 등록 요청")
public record ResourceReportCreateRequest(
        @Schema(description = "자원 요청 유형", example = "요청")
        @NotNull(message = "자원 요청 유형은 필수입니다")
        ResourceReportType type,

        @Schema(description = "자원 요청 카테고리", example = "식량")
        @NotNull(message = "자원 요청 카테고리는 필수입니다")
        ResourceReportCategory category,

        @Schema(description = "자원 요청 제목", example = "식량 지원이 필요합니다")
        @NotBlank(message = "자원 요청 제목은 필수입니다")
        String title,

        @Schema(description = "자원 요청 상세 내용", example = "재해로 인해 식량이 부족합니다. 라면, 물 등이 필요합니다.")
        @NotBlank(message = "자원 요청 상세 내용은 필수입니다")
        String description,

        @Schema(description = "위치", example = "서울시 강남구 역삼동")
        @NotBlank(message = "위치는 필수입니다")
        String location,

        @Schema(description = "S3 업로드된 파일 URL 목록")
        List<String> fileUrls
) {
}