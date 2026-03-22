package Hongik_SafeMap_Server.domain.disaster_report.dto.request;

import Hongik_SafeMap_Server.vo.DisasterType;
import Hongik_SafeMap_Server.vo.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DisasterReportCreateRequest(
        @NotNull
        DisasterType disasterType,
        @NotNull
        RiskLevel riskLevel,

        @NotBlank
        @Size(max = 1000)
        String disasterDescription,

        @NotNull
        Double latitude,
        @NotNull
        Double longitude,

        @Size(max = 255)
        String address,

        List<String> fileUrls
        ) {}
