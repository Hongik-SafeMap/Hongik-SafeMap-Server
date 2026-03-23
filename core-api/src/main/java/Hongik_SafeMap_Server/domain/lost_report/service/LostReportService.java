package Hongik_SafeMap_Server.domain.lost_report.service;

import Hongik_SafeMap_Server.domain.lost_report.domain.LostReport;
import Hongik_SafeMap_Server.domain.lost_report.domain.LostReportComment;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCommentCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportCreateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportStatusPatchRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.request.LostReportUpdateRequest;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportCommentResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportCommentsResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportResponse;
import Hongik_SafeMap_Server.domain.lost_report.dto.response.LostReportsPageResponse;
import Hongik_SafeMap_Server.domain.lost_report.repository.LostReportCommentRepository;
import Hongik_SafeMap_Server.domain.lost_report.repository.LostReportRepository;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.exception.ErrorMessage;
import Hongik_SafeMap_Server.exception.LostReportException;
import Hongik_SafeMap_Server.global.dto.response.LostReportWithCommentCount;
import Hongik_SafeMap_Server.util.MemberUtil;
import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.LOST_REPORT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LostReportService {

    private final LostReportRepository lostReportRepository;
    private final LostReportCommentRepository lostReportCommentRepository;
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
        LostReport lostReport = lostReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        long commentCount = lostReportCommentRepository.countByLostReportId(id);

        Member currentMember = memberUtil.getLoggedInMember();
        boolean isAuthor = lostReport.getMember().getId().equals(currentMember.getId());

        return LostReportResponse.of(lostReport, commentCount, isAuthor);
    }

    public LostReportsPageResponse getLostReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return createLostReportsPageResponse(lostReportRepository.findAllWithCommentCount(pageable));
    }

    public LostReportsPageResponse getLostReportsByCategory(LostReportCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return createLostReportsPageResponse(lostReportRepository.findByCategoryWithCommentCount(category, pageable));
    }

    public LostReportsPageResponse getLostReportsByStatus(LostReportStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return createLostReportsPageResponse(lostReportRepository.findByStatusWithCommentCount(status, pageable));
    }

    public LostReportsPageResponse getLostReportsByCategoryAndStatus(LostReportCategory category, LostReportStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return createLostReportsPageResponse(lostReportRepository.findByCategoryAndStatusWithCommentCount(category, status, pageable));
    }

    private LostReportsPageResponse createLostReportsPageResponse(Page<LostReportWithCommentCount> page) {
        Member currentMember = memberUtil.getLoggedInMember();

        List<LostReportResponse> reportResponses = page.getContent().stream()
                .map(dto -> {
                    boolean isAuthor = dto.lostReport().getMember().getId().equals(currentMember.getId());
                    return LostReportResponse.of(dto.lostReport(), dto.commentCount(), isAuthor);
                })
                .toList();

        return new LostReportsPageResponse(
                reportResponses,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Transactional
    public Long createComment(Long lostReportId, LostReportCommentCreateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        LostReport lostReport = lostReportRepository.findByIdAndNotDeleted(lostReportId)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        LostReportComment comment = LostReportComment.builder()
                .content(request.content())
                .member(member)
                .lostReport(lostReport)
                .build();

        LostReportComment savedComment = lostReportCommentRepository.save(comment);
        return savedComment.getId();
    }

    @Transactional
    public LostReportCommentsResponse getComments(Long lostReportId) {
        lostReportRepository.findByIdAndNotDeleted(lostReportId)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        List<LostReportComment> comments = lostReportCommentRepository.findByLostReportIdOrderByCreatedAtAsc(lostReportId);

        List<LostReportCommentResponse> commentResponses = comments.stream()
                .map(LostReportCommentResponse::of)
                .toList();

        return new LostReportCommentsResponse(
                comments.size(),
                commentResponses
        );
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberUtil.getLoggedInMember();

        LostReport lostReport = lostReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        // 작성자 본인인지 확인
        if (!lostReport.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.REPORT_DELETE_UNAUTHORIZED);
        }

        lostReport.softDelete();
        lostReportRepository.save(lostReport);
    }

    @Transactional
    public LostReportResponse update(Long id, LostReportUpdateRequest request) {
        Member member = memberUtil.getLoggedInMember();

        LostReport lostReport = lostReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        // 작성자 본인인지 확인
        if (!lostReport.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.REPORT_UPDATE_UNAUTHORIZED);
        }

        lostReport.update(
                request.category(),
                request.status(),
                request.title(),
                request.description(),
                request.age(),
                request.characteristic(),
                request.lastSeen(),
                request.currentLocation(),
                request.fileUrls()
        );

        LostReport updatedReport = lostReportRepository.save(lostReport);
        long commentCount = lostReportCommentRepository.countByLostReportId(id);

        boolean isAuthor = true;
        return LostReportResponse.of(updatedReport, commentCount, isAuthor);
    }

    @Transactional
    public LostReportResponse updateStatus(Long id, LostReportStatusPatchRequest request) {
        Member member = memberUtil.getLoggedInMember();

        LostReport lostReport = lostReportRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new LostReportException(LOST_REPORT_NOT_FOUND));

        // 작성자 본인인지 확인
        if (!lostReport.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.REPORT_UPDATE_UNAUTHORIZED);
        }

        lostReport.updateStatus(request.status());

        LostReport updatedReport = lostReportRepository.save(lostReport);
        long commentCount = lostReportCommentRepository.countByLostReportId(id);

        boolean isAuthor = true;
        return LostReportResponse.of(updatedReport, commentCount, isAuthor);
    }
}