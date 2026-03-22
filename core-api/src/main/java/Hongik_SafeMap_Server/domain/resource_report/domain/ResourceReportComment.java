package Hongik_SafeMap_Server.domain.resource_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_report_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_comment_id")
    private Long id;

    @Column(name = "resource_comment_content", length = 255, nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_request_id", nullable = false)
    private ResourceReport resourceReport;

    @Builder
    public ResourceReportComment(String content, Member member, ResourceReport resourceReport) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.resourceReport = resourceReport;
    }
}