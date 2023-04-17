package com.developers.member.attach.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Log4j2
@Component
@RestControllerAdvice
public class FileUploadFailedException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.info("[FileUploadFailedException] handleMaxUploadSizeExceededException: {}", e);
        ErrorResponse response = ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "용량 초과").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}