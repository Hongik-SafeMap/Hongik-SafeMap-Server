package Hongik_SafeMap_Server.domain.disaster_report.domain;

import Hongik_SafeMap_Server.vo.DisasterReportEvaluationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisasterReportEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int helpfulCount;
    
    @Column(nullable = false)
    private int notHelpfulCount;

    @OneToOne
    @MapsId // 식별 관계
    @JoinColumn(name = "disaster_report_id")
    private DisasterReport disasterReport;

    public DisasterReportEvaluation(DisasterReport disasterReport) {
        this.disasterReport = disasterReport;
        this.helpfulCount = 0;
        this.notHelpfulCount = 0;
    }

    public void increase(DisasterReportEvaluationType evaluationType) {
        switch (evaluationType) {
            case HELPFUL -> helpfulCount++;
            case NOT_HELPFUL -> notHelpfulCount++;
        }
    }

    public void decrease(DisasterReportEvaluationType evaluationType) {
        switch (evaluationType) {
            case HELPFUL -> helpfulCount--;
            case NOT_HELPFUL -> notHelpfulCount--;
        }
    }
}
