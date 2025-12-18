package Hongik_SafeMap_Server.domain.member.sensitive_info.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensitiveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensitive_info_id")
    private Long id;

    // 회원 1명당 민감정보는 1개
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    // 의료 정보
    @Column(length = 10)
    private String bloodType;

    @Column( length = 500)
    private String allergies;

    @Column(length = 500)
    private String chronicDiseases;

    @Column(length = 500)
    private String medications;

    @Builder
    public SensitiveInfo(Member member, String bloodType, String allergies, String chronicDiseases, String medications) {
        this.member = member;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.chronicDiseases = chronicDiseases;
        this.medications = medications;
    }

    // 민감정보 수정
    public void updateSensitiveInfo(String bloodType, String allergies, String chronicDiseases, String medications) {
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.chronicDiseases = chronicDiseases;
        this.medications = medications;
    }
}
