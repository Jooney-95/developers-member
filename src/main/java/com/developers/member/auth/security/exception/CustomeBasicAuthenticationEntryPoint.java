package com.developers.member.auth.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class CustomeBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + super.getRealmName() + "");
        String errorMessage = "";
        if (authException instanceof UsernameNotFoundException) {
            errorMessage = "Invalid username or password";
        } else {
            errorMessage = authException.getMessage();
        }
        log.info("[CustomeBasicAuthenticationEntryPoint] HTTP Status " + HttpStatus.UNAUTHORIZED.value() + " - " + errorMessage);
        PrintWriter writer = response.getWriter();
//        writer.println("HTTP Status 401 - " + authException.getMessage());
        writer.println("HTTP Status " + HttpStatus.UNAUTHORIZED.value() + " - " + errorMessage);

    }

    @Override
    public void afterPropertiesSet() {
        super.setRealmName("developers-member");
        super.afterPropertiesSet();
    }
}
