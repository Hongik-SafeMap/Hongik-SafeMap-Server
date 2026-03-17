package Hongik_SafeMap_Server.domain.lost_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_report_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "실종신고댓글아이디")
    private Long id;

    @Column(name = "실종신고댓글", length = 255, nullable = false)
    private String content;

    @Column(name = "댓글생성시간", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "회원아이디", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "실종신고아아디", nullable = false)
    private LostReport lostReport;

    @Builder
    public LostReportComment(String content, Member member, LostReport lostReport) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.lostReport = lostReport;
    }
}