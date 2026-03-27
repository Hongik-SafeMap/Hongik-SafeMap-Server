package Hongik_SafeMap_Server.domain.disaster_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "disaster_report_id"})
})
public class UserEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_report_id", nullable = false)
    private DisasterReport disasterReport;

    @Column(name = "is_helpful", nullable = false)
    private Boolean isHelpful = false;

    @Column(name = "is_not_helpful", nullable = false)
    private Boolean isNotHelpful = false;

    @Column(name = "is_accurate", nullable = false)
    private Boolean isAccurate = false;

    @Column(name = "is_false_report", nullable = false)
    private Boolean isFalseReport = false;

    public UserEvaluation(Member member, DisasterReport disasterReport) {
        this.member = member;
        this.disasterReport = disasterReport;
        this.isHelpful = false;
        this.isNotHelpful = false;
        this.isAccurate = false;
        this.isFalseReport = false;
    }

    // 값이 들어온 필드만 업데이트
    public void updateEvaluations(Boolean isHelpful, Boolean isNotHelpful, Boolean isAccurate, Boolean isFalseReport) {
        if (isHelpful != null) this.isHelpful = isHelpful;
        if (isNotHelpful != null) this.isNotHelpful = isNotHelpful;
        if (isAccurate != null) this.isAccurate = isAccurate;
        if (isFalseReport != null) this.isFalseReport = isFalseReport;
    }
}