package Hongik_SafeMap_Server.domain.admin.disaster_review;

import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {
    private final DisasterReportRepository disasterReportRepository;

    // 제보 검토 - 전체 제보 목록
    public Page<AdminReportListResponse> findAllReports(Pageable pageable) {
        return disasterReportRepository.findAll(pageable)
                .map(AdminReportListResponse::of);
    }
}
