package Hongik_SafeMap_Server.domain.disaster_report.dto.request;

import Hongik_SafeMap_Server.vo.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DisasterReportCreateRequest(
        @Schema(description = "재난 유형", example = "화재")
        @NotNull
        Long disasterTypeId,

        @NotNull
        RiskLevel riskLevel,

        @Schema(description = "재난 상황 설명", example = "ㅇㅇ 건물 2층에서 화재가 발생했습니다")
        @NotBlank
        @Size(max = 1000)
        String disasterDescription,

        @Schema(description = "위도", example = "37.5665")
        @NotNull
        Double latitude,

        @Schema(description = "경도", example = "126.9780")
        @NotNull
        Double longitude,

        @Schema(description = "주소", example = "서울특별시 강남구 역삼동")
        @Size(max = 255)
        String address,

        @Schema(description = "첨부 파일 URL 목록", example = "[]")
        List<String> fileUrls
) {
}
