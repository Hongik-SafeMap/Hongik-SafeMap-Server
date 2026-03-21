package Hongik_SafeMap_Server.global.dto.response;

import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;

public record ResourceReportWithCommentCount(
        ResourceReport resourceReport,
        Long commentCount
) {
}