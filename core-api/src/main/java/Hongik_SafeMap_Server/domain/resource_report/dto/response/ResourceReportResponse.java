package Hongik_SafeMap_Server.domain.resource_report.dto.response;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import Hongik_SafeMap_Server.vo.ResourceReportType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "자원 요청/공급 응답")
public record ResourceReportResponse(
        @Schema(description = "자원 요청 아이디")
        Long id,

        @Schema(description = "자원 요청 유형")
        ResourceReportType type,

        @Schema(description = "자원요청 카테고리")
        ResourceReportCategory category,

        @Schema(description = "자원 요청 제목")
        String title,

        @Schema(description = "자원 요청 상세 내용")
        String description,

        @Schema(description = "위치")
        String location,

        @Schema(description = "자원 요청 상태")
        ResourceReportStatus status,

        @Schema(description = "생성 시각")
        LocalDateTime createdAt,

        @Schema(description = "파일 URL 목록")
        List<String> fileUrls,

        @Schema(description = "회원 이름")
        String memberName,

        @Schema(description = "작성자 여부", example = "true")
        boolean isAuthor
) {
    public static ResourceReportResponse from(ResourceReport resourceReport, boolean isAuthor) {
        return new ResourceReportResponse(
                resourceReport.getId(),
                resourceReport.getType(),
                resourceReport.getCategory(),
                resourceReport.getTitle(),
                resourceReport.getDescription(),
                resourceReport.getLocation(),
                resourceReport.getStatus(),
                resourceReport.getCreatedAt(),
                resourceReport.getFileUrls(),
                resourceReport.getMember().getName(),
                isAuthor
        );
    }
}