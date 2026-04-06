package Hongik_SafeMap_Server.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAdminActivity {

    /**
     * 관리자가 수행한 행동에 대한 설명
     */
    String description();
}