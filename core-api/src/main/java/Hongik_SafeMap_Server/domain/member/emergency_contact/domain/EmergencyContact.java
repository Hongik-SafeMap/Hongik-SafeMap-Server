package Hongik_SafeMap_Server.domain.member.emergency_contact.domain;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergency_contact_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 30)
    private String relationship;

    @Column(length = 30, nullable = false)
    private String phone;

    @Builder
    public EmergencyContact(Member member, String name, String relationship, String phone) {
        this.member = member;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    // 비상연락처 추가
    public void updateEmergencyContact(String name, String relationship, String phone) {
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }
}
