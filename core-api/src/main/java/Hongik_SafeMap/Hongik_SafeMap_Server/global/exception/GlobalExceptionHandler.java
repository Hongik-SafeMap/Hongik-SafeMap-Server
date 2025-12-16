package Hongik_SafeMap.Hongik_SafeMap_Server.global.exception;

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
        log.info("MemberException: {}", ex.getMessage());

        return ErrorResponse.create(
                ex,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }
}
