package Hongik_SafeMap_Server.util;

public class EnumUtil {

    public static <E extends Enum<E> & DescriptionProvider> E fromDescription(Class<E> enumClass, String description) {
        if (description == null) {
            return null;
        }

        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getDescription().equals(description)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("[" + description + "]은 유효하지 않은 " + getKoreanName(enumClass) + " 값입니다");
    }

    private static String getKoreanName(Class<?> enumClass) {
        String simpleName = enumClass.getSimpleName();
        return switch (simpleName) {
            case "LostReportCategory" -> "카테고리";
            case "LostReportStatus" -> "상태";
            case "ResourceReportType" -> "유형";
            case "ResourceReportCategory" -> "카테고리";
            case "ResourceReportStatus" -> "상태";
            default -> simpleName;
        };
    }

    public interface DescriptionProvider {
        String getDescription();
    }
}