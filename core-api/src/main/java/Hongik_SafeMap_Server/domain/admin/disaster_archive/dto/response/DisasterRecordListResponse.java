package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import Hongik_SafeMap_Server.domain.disaster_report_group.dto.response.GroupedDisasterReportResponse;

import java.util.List;

public record DisasterRecordListResponse(
        long total,
        List<GroupedDisasterReportResponse> disasterRecords
) {
    public static DisasterRecordListResponse of(List<GroupedDisasterReportResponse> records) {
        return new DisasterRecordListResponse(records.size(), records);
    }
}
