package Hongik_SafeMap_Server.config;

import Hongik_SafeMap_Server.vo.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 실종신고 컨버터
        registry.addConverter(new LostReportCategoryConverter());
        registry.addConverter(new LostReportStatusConverter());

        // 자원요청 컨버터
        registry.addConverter(new ResourceReportTypeConverter());
        registry.addConverter(new ResourceReportCategoryConverter());
        registry.addConverter(new ResourceReportStatusConverter());

        // 재난제보 컨버터
        registry.addConverter(new DisasterTypeConverter());
        registry.addConverter(new RiskLevelConverter());
        registry.addConverter(new DisasterReportStatusConverter());
    }

    private static class LostReportCategoryConverter implements Converter<String, LostReportCategory> {
        public LostReportCategory convert(String source) {
            return LostReportCategory.fromDescription(source);
        }
    }

    private static class LostReportStatusConverter implements Converter<String, LostReportStatus> {
        public LostReportStatus convert(String source) {
            return LostReportStatus.fromDescription(source);
        }
    }

    private static class ResourceReportTypeConverter implements Converter<String, ResourceReportType> {
        public ResourceReportType convert(String source) {
            return ResourceReportType.fromDescription(source);
        }
    }

    private static class ResourceReportCategoryConverter implements Converter<String, ResourceReportCategory> {
        public ResourceReportCategory convert(String source) {
            return ResourceReportCategory.fromDescription(source);
        }
    }

    private static class ResourceReportStatusConverter implements Converter<String, ResourceReportStatus> {
        public ResourceReportStatus convert(String source) {
            return ResourceReportStatus.fromDescription(source);
        }
    }

    private static class DisasterTypeConverter implements Converter<String, DisasterType> {
        public DisasterType convert(String source) {
            return DisasterType.fromDescription(source);
        }
    }

    private static class RiskLevelConverter implements Converter<String, RiskLevel> {
        public RiskLevel convert(String source) {
            return RiskLevel.fromDescription(source);
        }
    }

    private static class DisasterReportStatusConverter implements Converter<String, DisasterReportStatus> {
        public DisasterReportStatus convert(String source) {
            return DisasterReportStatus.fromDescription(source);
        }
    }
}