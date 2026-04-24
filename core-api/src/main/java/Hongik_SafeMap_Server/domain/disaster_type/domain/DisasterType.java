package Hongik_SafeMap_Server.domain.disaster_type.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disaster_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisasterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disaster_type_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 500)
    private String iconUrl;

    @Builder
    private DisasterType(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
