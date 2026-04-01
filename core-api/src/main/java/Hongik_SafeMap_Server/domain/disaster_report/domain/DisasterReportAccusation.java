package Hongik_SafeMap_Server.domain.disaster_report.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"member_id", "disaster_report_id"}
    )
)
public class DisasterReportAccusation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disaster_report_accusation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_report_id", nullable = false)
    private DisasterReport disasterReport;

    @Builder
    private DisasterReportAccusation(
            Member member,
            DisasterReport disasterReport
    ) {
        this.member = member;
        this.disasterReport = disasterReport;
    }
}