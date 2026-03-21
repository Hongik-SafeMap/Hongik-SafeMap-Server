package Hongik_SafeMap_Server.domain.resource_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.ResourceReportCategory;
import Hongik_SafeMap_Server.vo.ResourceReportStatus;
import Hongik_SafeMap_Server.vo.ResourceReportType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resource_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_request_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_request_type", nullable = false)
    private ResourceReportType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_request_category", nullable = false)
    private ResourceReportCategory category;

    @Column(name = "resource_request_title", length = 255, nullable = false)
    private String title;

    @Column(name = "resource_request_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "current_location", length = 255, nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_request_status", nullable = false)
    private ResourceReportStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ElementCollection
    @CollectionTable(name = "resource_report_file", joinColumns = @JoinColumn(name = "resource_request_id"))
    @Column(name = "file_url", length = 500)
    private List<String> fileUrls = new ArrayList<>();

    @Builder
    public ResourceReport(ResourceReportType type, ResourceReportCategory category, String title,
                          String description, String location, Member member, List<String> fileUrls) {
        this.type = type;
        this.category = category;
        this.title = title;
        this.description = description;
        this.location = location;
        this.status = ResourceReportStatus.IN_PROGRESS;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.fileUrls = fileUrls != null ? new ArrayList<>(fileUrls) : new ArrayList<>();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void updateStatus(ResourceReportStatus status) {
        this.status = status;
    }

    public void update(ResourceReportType type, ResourceReportCategory category, ResourceReportStatus status,
                       String title, String description, String location, List<String> fileUrls) {
        this.type = type;
        this.category = category;
        this.status = status;
        this.title = title;
        this.description = description;
        this.location = location;
        this.fileUrls = fileUrls != null ? new ArrayList<>(fileUrls) : new ArrayList<>();
    }
}