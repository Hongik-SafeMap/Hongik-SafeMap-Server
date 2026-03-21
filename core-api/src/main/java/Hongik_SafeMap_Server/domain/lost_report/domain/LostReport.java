package Hongik_SafeMap_Server.domain.lost_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lost_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lost_report_category", nullable = false)
    private LostReportCategory category;

    @Column(name = "lost_report_title", length = 255, nullable = false)
    private String title;

    @Column(name = "lost_report_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "age", length = 20)
    private String age;

    @Column(name = "characteristic", length = 255, nullable = false)
    private String characteristic;

    @Column(name = "last_seen", length = 255, nullable = false)
    private String lastSeen;

    @Column(name = "current_location", length = 255, nullable = false)
    private String currentLocation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "lost_report_status", nullable = false)
    private LostReportStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ElementCollection
    @CollectionTable(name = "lost_report_file", joinColumns = @JoinColumn(name = "lost_report_id"))
    @Column(name = "file_url", length = 500)
    private List<String> fileUrls = new ArrayList<>();

    @Builder
    public LostReport(LostReportCategory category, String title, String description, String age,
                      String characteristic, String lastSeen, String currentLocation,
                      Member member, List<String> fileUrls) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.age = age;
        this.characteristic = characteristic;
        this.lastSeen = lastSeen;
        this.currentLocation = currentLocation;
        this.createdAt = LocalDateTime.now();
        this.status = LostReportStatus.IN_PROGRESS;
        this.member = member;
        this.fileUrls = fileUrls != null ? new ArrayList<>(fileUrls) : new ArrayList<>();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}