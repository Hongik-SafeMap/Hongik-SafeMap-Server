package Hongik_SafeMap_Server.domain.disaster_report.domain;

import Hongik_SafeMap_Server.domain.common.BaseTimeEntity;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisasterReport extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disaster_report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DisasterType disasterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(name = "disaster_description", nullable = false, length = 1000)
    private String disasterDescription;

    // 위치 정보 (필수)
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    // 주소 문자열 (보조)
    @Column(length = 255)
    private String address;

    // 파일 URL 목록
    @ElementCollection
    @CollectionTable(
            name = "disaster_report_file",
            joinColumns = @JoinColumn(name = "disaster_report_id")
    )
    @Column(name = "file_url", length = 2048)
    private List<String> fileUrls = new ArrayList<>();

    // 제보 처리 상태(관리자 검토/조치)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DisasterReportStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private DisasterReport(
            DisasterType disasterType,
            RiskLevel riskLevel,
            String disasterDescription,
            Double latitude,
            Double longitude,
            String address,
            List<String> fileUrls,
            DisasterReportStatus status,
            Member member
    ) {
        this.disasterType = disasterType;
        this.riskLevel = riskLevel;
        this.disasterDescription = disasterDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.fileUrls = (fileUrls == null) ? new ArrayList<>() : fileUrls;
        this.status = (status == null) ? DisasterReportStatus.PENDING : status;
        this.member = member;
    }

    /**
     * 필요하면 setter 대신 의미있는 메서드로만 상태 변경
     */
    public void approve() {
        this.status = DisasterReportStatus.APPROVED;
    }

    public void blind() {
        this.status = DisasterReportStatus.BLINDED;
    }
}
