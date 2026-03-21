package Hongik_SafeMap_Server.global.dto.response;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReport;

public record LostReportWithCommentCount(
        LostReport lostReport,
        Long commentCount
) {
}