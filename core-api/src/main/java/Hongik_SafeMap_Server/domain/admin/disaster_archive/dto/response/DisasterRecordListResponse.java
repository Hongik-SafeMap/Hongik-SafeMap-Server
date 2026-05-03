package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupedDisasterReportResponse;

import java.util.List;

public record DisasterRecordListResponse(
        List<GroupedDisasterReportResponse> disasterRecords,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
