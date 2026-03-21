package Hongik_SafeMap_Server.domain.resource_report.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReportComment;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportResponse;
import Hongik_SafeMap_Server.domain.resource_report.repository.ResourceReportCommentRepository;
import Hongik_SafeMap_Server.domain.resource_report.repository.ResourceReportRepository;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.exception.ResourceReportException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceReportService {

    private final ResourceReportRepository resourceReportRepository;
    private final ResourceReportCommentRepository resourceReportCommentRepository;
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

    public ResourceReportResponse findById(Long id) {
        ResourceReport resourceReport = resourceReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceReportException(ErrorMessage.RESOURCE_REPORT_NOT_FOUND));

        return ResourceReportResponse.from(resourceReport);
    }

    @Transactional
    public Long createComment(Long resourceReportId, ResourceReportCommentCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        ResourceReport resourceReport = resourceReportRepository.findByIdAndNotDeleted(resourceReportId)
                .orElseThrow(() -> new ResourceReportException(ErrorMessage.RESOURCE_REPORT_NOT_FOUND));

        ResourceReportComment comment = ResourceReportComment.builder()
                .content(request.content())
                .member(member)
                .resourceReport(resourceReport)
                .build();

        ResourceReportComment savedComment = resourceReportCommentRepository.save(comment);
        return savedComment.getId();
    }
}