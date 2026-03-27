package Hongik_SafeMap_Server.domain.disaster_report_group.domain;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
    private Boolean isActive;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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
                                 LocalDateTime latestTime, int reportCount, RiskLevel latestRisk, boolean isActive) {
        this.centerLatitude = centerLat;
        this.centerLongitude = centerLng;
        this.earliestReportTime = earliestTime;
        this.latestReportTime = latestTime;
        this.reportCount = reportCount;
        this.latestRiskLevel = latestRisk;
        this.isActive = isActive;
    }

    // 그룹이 새로운 제보와 매칭되는지 확인
    public boolean canAcceptReport(DisasterReport report, int maxDistanceM, int maxTimeSpanHours) {
        if (!this.isActive) {
            return false;
        }

        // 재난 타입 일치 확인
        if (!this.disasterType.equals(report.getDisasterType())) {
            return false;
        }

        // 거리 확인 (위도/경도 기반)
        double distance = calculateDistance(this.centerLatitude, this.centerLongitude,
                report.getLatitude(), report.getLongitude());
        if (distance > maxDistanceM) {
            return false;
        }

        // 시간 범위 확인
        LocalDateTime reportTime = report.getCreatedAt();
        long hoursDiff = java.time.Duration.between(this.latestReportTime, reportTime).toHours(); // 내림(24시간 30분 -> 24시간)

        return hoursDiff < maxTimeSpanHours; // 정확히 maxTimeSpanHours 차이나면 기존 재난 제보에 추가 안함
    }

    // 두 좌표 간 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371; // 지구 반지름 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c * 1000; // 미터(m)
    }
}