package com.developers.member.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(value = "authenticationFailureHandler")
@Log4j2
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 실패로직 핸들링
        log.error("[LoginFailureHandler] 로그인 실패!");
        exception.printStackTrace();
        writePrintErrorResponse(response, exception);
    }

    private void writePrintErrorResponse(HttpServletResponse response, AuthenticationException exception) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = new HashMap<>();
            String message = getExceptionMessage(exception);
            responseMap.put("status", 401);
            responseMap.put("message", message);
            response.getOutputStream().println(objectMapper.writeValueAsString(responseMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExceptionMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            return "존재하지 않는 사용자입니다.";
        } else {
            return "확인된 에러가 없습니다.";
        }
    }
}
