package Hongik_SafeMap_Server.config;

import Hongik_SafeMap_Server.vo.LostReportCategory;
import Hongik_SafeMap_Server.vo.LostReportStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LostReportCategoryConverter());
        registry.addConverter(new LostReportStatusConverter());
    }

    private static class LostReportCategoryConverter implements Converter<String, LostReportCategory> {
        @Override
        public LostReportCategory convert(String source) {
            return LostReportCategory.fromDescription(source);
        }
    }

    private static class LostReportStatusConverter implements Converter<String, LostReportStatus> {
        @Override
        public LostReportStatus convert(String source) {
            return LostReportStatus.fromDescription(source);
        }
    }
}