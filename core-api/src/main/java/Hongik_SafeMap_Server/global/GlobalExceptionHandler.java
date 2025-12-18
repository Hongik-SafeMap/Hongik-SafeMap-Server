package Hongik_SafeMap_Server.global;

import Hongik_SafeMap_Server.exception.AuthException;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import Hongik_SafeMap_Server.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //@Valid 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("유효성 검사 실패");

        log.info("유효성 검사 실패: {}", errorMessage);

        return ErrorResponse.create(
                ex,
                HttpStatus.BAD_REQUEST,
                errorMessage
        );
    }

    // Member 도메인 예외처리(400)
    @ExceptionHandler(MemberException.class)
    public ErrorResponse handleMemberException(MemberException ex) {
        log.info("exception.MemberException: {}", ex.getMessage());

        return ErrorResponse.create(
                ex,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // Auth 도메인 예외처리(403)
    @ExceptionHandler(AuthException.class)
    public ErrorResponse handleDisasterReportException(AuthException ex) {
        log.info("exception.AuthException: {}", ex.getMessage());

        return ErrorResponse.create(
                ex,
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
    }

    // DisasterReport 도메인 예외처리(404)
    @ExceptionHandler(DisasterReportException.class)
    public ErrorResponse handleDisasterReportException(DisasterReportException ex) {
        log.info("exception.DisasterReportException: {}", ex.getMessage());

        return ErrorResponse.create(
                ex,
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }
}
