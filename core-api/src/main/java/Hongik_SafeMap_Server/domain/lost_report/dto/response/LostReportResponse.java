package Hongik_SafeMap_Server.domain.lost_report.dto.response;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReport;
import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "실종신고 조회 응답")
public record LostReportResponse(
        @Schema(description = "실종신고 ID", example = "1")
        Long id,

        @Schema(description = "카테고리", example = "사람")
        LostReportCategory category,

        @Schema(description = "제목", example = "할머니를 찾습니다")
        String title,

        @Schema(description = "상세 설명", example = "실종 상황을 설명...")
        String description,

        @Schema(description = "나이/연령", example = "72세")
        String age,

        @Schema(description = "특징", example = "흰머리, 빨간 외투")
        String characteristic,

        @Schema(description = "마지막 목격 장소", example = "서울시 강남구 역삼동")
        String lastSeen,

        @Schema(description = "현재 위치", example = "서울시 강남구 역삼동")
        String currentLocation,

        @Schema(description = "작성 시각")
        LocalDateTime createdAt,

        @Schema(description = "상태", example = "진행중")
        LostReportStatus status,

        @Schema(description = "파일 URL 목록")
        List<String> fileUrls,

        @Schema(description = "신고자 ID", example = "1")
        Long memberId,

        @Schema(description = "신고자 이름", example = "홍길동")
        String memberName,

        @Schema(description = "댓글 개수", example = "5")
        long commentCount,

        @Schema(description = "작성자 여부", example = "true")
        boolean isAuthor
) {
    public static LostReportResponse of(LostReport lostReport, long commentCount, boolean isAuthor) {
        return new LostReportResponse(
                lostReport.getId(),
                lostReport.getCategory(),
                lostReport.getTitle(),
                lostReport.getDescription(),
                lostReport.getAge(),
                lostReport.getCharacteristic(),
                lostReport.getLastSeen(),
                lostReport.getCurrentLocation(),
                lostReport.getCreatedAt(),
                lostReport.getStatus(),
                lostReport.getFileUrls(),
                lostReport.getMember().getId(),
                lostReport.getMember().getName(),
                commentCount,
                isAuthor
        );
    }
}
