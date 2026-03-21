package Hongik_SafeMap_Server.domain.resource_report.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.repository.ResourceReportRepository;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceReportService {

    private final ResourceReportRepository resourceReportRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public Long create(ResourceReportCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        ResourceReport resourceReport = ResourceReport.builder()
                .type(request.type())
                .category(request.category())
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .member(member)
                .fileUrls(request.fileUrls())
                .build();

        ResourceReport savedReport = resourceReportRepository.save(resourceReport);
        return savedReport.getId();
    }
}