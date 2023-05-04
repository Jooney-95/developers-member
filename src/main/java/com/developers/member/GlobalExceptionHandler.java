package com.developers.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> userNameNotFoundException(UsernameNotFoundException e) {
        log.error("[GlobalExceptionHadler]: 존재하지 않는 사용자입니다. {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다. "+e.getMessage());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException(AuthenticationException e) {
        log.error("[GlobalExceptionHadler]: 사용자 인증에 실패했습니다. {}", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증에 실패했습니다. "+e.getMessage());
    }

    // dto로 인지값이 전달되고 있고, @Valid로 체킹하고 있지만 기본적으로 매개변수 오류에 대한 로깅이 들어온다고 함
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        log.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("허용되지 않은 매개변수 값이 입력 되었습니다. "+e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) // @valid 오류
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("매개변수 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 매개변수 값이 입력 되었습니다. " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        log.error("API 요청 오류: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 API 요청입니다. " + e.getMessage());
    }
    @ExceptionHandler(Exception.class) //나머지 예외 처리
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error("비정상적 예외 발생: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비정상적인 예외가 발생했습니다. " + e.getMessage());
    }
}
