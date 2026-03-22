package Hongik_SafeMap_Server.domain.admin.disaster_review;

import Hongik_SafeMap_Server.domain.disaster_report.repository.DisasterReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {
    private final DisasterReportRepository disasterReportRepository;

    // 제보 검토 - 전체 제보 목록
    public AdminReportPageResponse findAllReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AdminReportListResponse> pageResult = disasterReportRepository.findAll(pageable)
                .map(AdminReportListResponse::of);
        
        List<AdminReportListResponse> reports = pageResult.getContent();
        
        return new AdminReportPageResponse(
                reports,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
    }
}
