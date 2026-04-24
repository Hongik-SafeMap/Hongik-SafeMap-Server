package Hongik_SafeMap_Server.domain.disaster_type.dto.response;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;

public record DisasterTypeResponse(
        Long id,
        String name,
        String iconUrl
) {
    public static DisasterTypeResponse of(DisasterType disasterType) {
        return new DisasterTypeResponse(disasterType.getId(), disasterType.getName(), disasterType.getIconUrl());
    }
}
