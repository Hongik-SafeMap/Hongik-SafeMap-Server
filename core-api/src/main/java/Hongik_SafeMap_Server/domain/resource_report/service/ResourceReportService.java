package Hongik_SafeMap_Server.domain.resource_report.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReport;
import Hongik_SafeMap_Server.domain.resource_report.domain.ResourceReportComment;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportCreateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.request.ResourceReportUpdateRequest;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportCommentResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportCommentsResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportResponse;
import Hongik_SafeMap_Server.domain.resource_report.dto.response.ResourceReportsPageResponse;
import Hongik_SafeMap_Server.domain.resource_report.repository.ResourceReportCommentRepository;
import Hongik_SafeMap_Server.domain.resource_report.repository.ResourceReportRepository;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.exception.ResourceReportException;
import Hongik_SafeMap_Server.global.dto.response.ResourceReportWithCommentCount;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import Hongik_SafeMap_Server.vo.ResourceReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Member currentMember = memberUtil.getLoggedInMember();
        boolean isAuthor = resourceReport.getMember().getId().equals(currentMember.getId());

        return ResourceReportResponse.from(resourceReport, isAuthor);
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

    public ResourceReportCommentsResponse getComments(Long resourceReportId) {
        resourceReportRepository.findByIdAndNotDeleted(resourceReportId)
                .orElseThrow(() -> new ResourceReportException(ErrorMessage.RESOURCE_REPORT_NOT_FOUND));

        List<ResourceReportComment> comments = resourceReportCommentRepository.findByResourceReportIdOrderByCreatedAtAsc(resourceReportId);

        List<ResourceReportCommentResponse> commentResponses = comments.stream()
                .map(ResourceReportCommentResponse::of)
                .toList();

        return new ResourceReportCommentsResponse(
                comments.size(),
                commentResponses
        );
    }

    @Transactional
    public ResourceReportResponse update(Long id, ResourceReportUpdateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        ResourceReport resourceReport = resourceReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceReportException(ErrorMessage.RESOURCE_REPORT_NOT_FOUND));

        // 작성자 본인인지 확인
        if (!resourceReport.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.REPORT_UPDATE_UNAUTHORIZED);
        }

        resourceReport.update(
                request.type(),
                request.category(),
                request.status(),
                request.title(),
                request.description(),
                request.location(),
                request.fileUrls()
        );

        ResourceReport updatedReport = resourceReportRepository.save(resourceReport);
        boolean isAuthor = true; // 수정은 작성자만 가능하므로 항상 true
        return ResourceReportResponse.from(updatedReport, isAuthor);
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberUtil.getLoggedInMember();

        ResourceReport resourceReport = resourceReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceReportException(ErrorMessage.RESOURCE_REPORT_NOT_FOUND));

        // 작성자 본인인지 확인
        if (!resourceReport.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.REPORT_DELETE_UNAUTHORIZED);
        }

        resourceReport.softDelete();
        resourceReportRepository.save(resourceReport);
    }

    public ResourceReportsPageResponse getResourceReports(Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findAllWithCommentCount(pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByType(ResourceReportType type, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByTypeWithCommentCount(type, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByCategory(ResourceReportCategory category, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByCategoryWithCommentCount(category, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByStatus(ResourceReportStatus status, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByStatusWithCommentCount(status, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByTypeAndCategory(ResourceReportType type, ResourceReportCategory category, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByTypeAndCategoryWithCommentCount(type, category, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByTypeAndStatus(ResourceReportType type, ResourceReportStatus status, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByTypeAndStatusWithCommentCount(type, status, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByCategoryAndStatus(ResourceReportCategory category, ResourceReportStatus status, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByCategoryAndStatusWithCommentCount(category, status, pageable));
    }

    public ResourceReportsPageResponse getResourceReportsByTypeAndCategoryAndStatus(ResourceReportType type, ResourceReportCategory category, ResourceReportStatus status, Pageable pageable) {
        return createResourceReportsPageResponse(resourceReportRepository.findByTypeAndCategoryAndStatusWithCommentCount(type, category, status, pageable));
    }

    private ResourceReportsPageResponse createResourceReportsPageResponse(Page<ResourceReportWithCommentCount> page) {
        Member currentMember = memberUtil.getLoggedInMember();
        
        List<ResourceReportResponse> reportResponses = page.getContent().stream()
                .map(dto -> {
                    boolean isAuthor = dto.resourceReport().getMember().getId().equals(currentMember.getId());
                    return ResourceReportResponse.from(dto.resourceReport(), isAuthor);
                })
                .toList();

        return new ResourceReportsPageResponse(
                reportResponses,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}