package Hongik_SafeMap_Server.domain.lost_report.dto.request;

import Hongik_SafeMap_Server.vo.LostReportCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "실종신고 등록 요청")
public record LostReportCreateRequest(
        @Schema(description = "카테고리", example = "사람")
        @NotNull(message = "카테고리는 필수입니다")
        LostReportCategory category,

        @Schema(description = "제목", example = "할머니를 찾습니다")
        @NotBlank(message = "제목은 필수입니다")
        String title,

        @Schema(description = "상세 설명", example = "실종 상황을 설명...")
        @NotBlank(message = "상세 설명은 필수입니다")
        String description,

        @Schema(description = "나이/연령", example = "72세")
        String age,

        @Schema(description = "특징", example = "흰머리, 빨간 외투")
        @NotBlank(message = "특징은 필수입니다")
        String characteristic,

        @Schema(description = "마지막 목격 장소", example = "서울시 강남구 역삼동")
        @NotBlank(message = "마지막 목격 장소는 필수입니다")
        String lastSeen,

        @Schema(description = "현재 위치 (알림 범위)", example = "서울시 강남구 역삼동")
        @NotBlank(message = "현재 위치는 필수입니다")
        String currentLocation,

        @Schema(description = "S3 업로드된 파일 URL 목록")
        List<String> fileUrls
) {
}