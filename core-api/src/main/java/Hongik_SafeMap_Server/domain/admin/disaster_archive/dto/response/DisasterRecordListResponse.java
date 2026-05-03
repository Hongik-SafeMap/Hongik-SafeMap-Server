package Hongik_SafeMap_Server.domain.admin.disaster_archive.dto.response;

import java.util.List;

public record DisasterRecordListResponse(
        List<DisasterArchiveRecordResponse> disasterRecords,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
