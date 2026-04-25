package Hongik_SafeMap_Server.domain.disaster_report_group.domain;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.util.DistanceUtil;
import Hongik_SafeMap_Server.vo.RiskLevel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "disaster_report_groups")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class DisasterReportGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    @Column(nullable = false)
    private Double centerLatitude;

    @Column(nullable = false)
    private Double centerLongitude;

    @Column(nullable = false)
    private LocalDateTime earliestReportTime;

    @Column(nullable = false)
    private LocalDateTime latestReportTime;

    @Column(nullable = false)
    private int reportCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel latestRiskLevel;

    @Column(nullable = false)
    private boolean isActive;

    @Column(length = 255)
    private String earliestAddress;

    /**
     * 첫 번째 제보로부터 새 그룹 생성
     */
    public static DisasterReportGroup createFromFirstReport(DisasterReport report) {
        return DisasterReportGroup.builder()
                .disasterType(report.getDisasterType())
                .centerLatitude(report.getLatitude())
                .centerLongitude(report.getLongitude())
                .earliestReportTime(report.getCreatedAt())
                .latestReportTime(report.getCreatedAt())
                .reportCount(1)
                .latestRiskLevel(report.getRiskLevel())
                .isActive(true)
                .earliestAddress(report.getAddress())
                .build();
    }

    // 새로운 제보 추가
    public void addReport(DisasterReport report) {
        report.updateGroup(this);
    }

    // 제보 제거시 그룹 참조 해제만 수행 (통계는 Service에서 별도 관리)
    public void removeReport(DisasterReport report) {
        report.updateGroup(null);
    }

    // 그룹 비활성화
    public void deactivate() {
        this.isActive = false;
    }

    // 그룹 통계 업데이트
    public void updateStatistics(double centerLat, double centerLng, LocalDateTime earliestTime,
                                 LocalDateTime latestTime, int reportCount, RiskLevel latestRisk,
                                 boolean isActive, String earliestAddress) {
        this.centerLatitude = centerLat;
        this.centerLongitude = centerLng;
        this.earliestReportTime = earliestTime;
        this.latestReportTime = latestTime;
        this.reportCount = reportCount;
        this.latestRiskLevel = latestRisk;
        this.isActive = isActive;
        this.earliestAddress = earliestAddress;
    }

    // 그룹이 새로운 제보와 매칭되는지 확인
    public boolean canAcceptReport(DisasterReport report, int maxDistanceM, int maxTimeSpanHours) {
        if (!this.isActive) {
            return false;
        }

        // 재난 타입 일치 확인
        if (!this.disasterType.getId().equals(report.getDisasterType().getId())) {
            return false;
        }

        // 거리 확인 (위도/경도 기반)
        double distance = DistanceUtil.calculateDistance(this.centerLatitude, this.centerLongitude,
                report.getLatitude(), report.getLongitude());
        if (distance > maxDistanceM) {
            return false;
        }

        // 시간 범위 확인
        LocalDateTime reportTime = report.getCreatedAt();
        long hoursDiff = java.time.Duration.between(this.latestReportTime, reportTime).toHours(); // 내림(24시간 30분 -> 24시간)

        return hoursDiff < maxTimeSpanHours; // 정확히 maxTimeSpanHours 차이나면 기존 재난 제보에 추가 안함
    }

}
