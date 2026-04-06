package Hongik_SafeMap_Server.global.aop;

import Hongik_SafeMap_Server.domain.admin.activity.service.AdminActivityLogService;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.global.annotation.LogAdminActivity;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminActivityLoggingAspect {

    private final AdminActivityLogService adminActivityLogService;
    private final MemberUtil memberUtil;

    @Around("@annotation(Hongik_SafeMap_Server.global.annotation.LogAdminActivity)")
    public Object logAdminActivity(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // 현재 로그인한 관리자 정보 가져오기
            Member currentAdmin = memberUtil.getLoggedInMember();

            // 어노테이션 정보 추출
            LogAdminActivity annotation = getLogAdminActivityAnnotation(joinPoint);
            if (annotation == null) {
                return joinPoint.proceed();
            }

            // 메서드 실행
            Object result = joinPoint.proceed();

            // 성공적으로 실행된 경우에만 로그 기록
            String fullDescription = "[" + currentAdmin.getAdminNickname() + "] " + annotation.description();

            adminActivityLogService.saveLog(
                    currentAdmin.getId(),
                    fullDescription
            );

            log.debug("Admin activity logged: {} by admin ID {}", fullDescription, currentAdmin.getId());

            return result;

        } catch (Exception e) {
            log.error("관리자 활동 로깅 실패", e);
            throw e;
        }
    }

    private LogAdminActivity getLogAdminActivityAnnotation(ProceedingJoinPoint joinPoint) {
        try {
            Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
            return method.getAnnotation(LogAdminActivity.class);
        } catch (Exception e) {
            log.warn("Failed to get LogAdminActivity annotation", e);
            return null;
        }
    }
}