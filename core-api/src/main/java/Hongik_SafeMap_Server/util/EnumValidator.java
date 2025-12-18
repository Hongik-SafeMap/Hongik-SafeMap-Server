package Hongik_SafeMap_Server.util;

import Hongik_SafeMap_Server.global.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null 값은 @NotNull 어노테이션에게 맡기고 통과
        if (value == null || value.isBlank()) {
            return true;
        }

        // 해당 Enum의 모든 상수
        Enum<?>[] enumConstants = enumClass.getEnumConstants();

        for (Enum<?> enumConstant : enumConstants) {
            // 영어 이름 비교(대소문자 무시)
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return true;
            }

            // 한글 description 비교(리플렉션 사용)
            try{
                Method method = enumConstant.getClass().getMethod("getDescription");
                String description = (String) method.invoke(enumConstant);

                if (description.equals(value)) {
                    return true;
                }
            } catch (Exception e) {
                // description 메서드가 없는 Enum일 경우 무시하고 영어 이름만 비교
            }
        }

        return false;
    }
}
