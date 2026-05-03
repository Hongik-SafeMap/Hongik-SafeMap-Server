package Hongik_SafeMap_Server.domain.notification.domain;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_preference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", nullable = false)
    private DisasterType disasterType;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @Builder
    public NotificationPreference(Member member, DisasterType disasterType, boolean isEnabled) {
        this.member = member;
        this.disasterType = disasterType;
        this.isEnabled = isEnabled;
    }

    public void updateEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
