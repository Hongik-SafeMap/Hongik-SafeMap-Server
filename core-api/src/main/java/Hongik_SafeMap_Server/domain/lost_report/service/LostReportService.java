package Hongik_SafeMap_Server.domain.lost_report.service;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReport;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportResponse;
import Hongik_SafeMap_Server.domain.lost_report.repository.LostReportRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.LostReportException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.LOST_REPORT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LostReportService {

    private final LostReportRepository lostReportRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public Long create(LostReportCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        LostReport lostReport = LostReport.builder()
                .category(request.category())
                .title(request.title())
                .description(request.description())
                .age(request.age())
                .characteristic(request.characteristic())
                .lastSeen(request.lastSeen())
                .currentLocation(request.currentLocation())
                .member(member)
                .fileUrls(request.fileUrls())
                .build();

        LostReport savedReport = lostReportRepository.save(lostReport);
        return savedReport.getId();
    }

    public LostReportResponse getById(Long id) {
        LostReport lostReport = lostReportRepository.findById(id)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));
        return LostReportResponse.of(lostReport);
    }
}