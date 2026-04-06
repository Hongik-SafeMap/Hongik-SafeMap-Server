package Hongik_SafeMap_Server.domain.disaster_report.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.DisasterReportEvaluationType;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private DisasterReportEvaluationType evaluationType;

    public UserEvaluation(Member member, DisasterReport disasterReport, DisasterReportEvaluationType evaluationType) {
        this.member = member;
        this.disasterReport = disasterReport;
        this.evaluationType = evaluationType;
    }

    public void updateEvaluation(DisasterReportEvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    public boolean hasEvaluation() {
        return this.evaluationType != null;
    }

    public boolean hasEvaluationType(DisasterReportEvaluationType type) {
        return this.evaluationType == type;
    }
}