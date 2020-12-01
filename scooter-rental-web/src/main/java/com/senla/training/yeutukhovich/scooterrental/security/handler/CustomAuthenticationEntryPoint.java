package com.senla.training.yeutukhovich.scooterrental.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.scooterrental.dto.ErrorDto;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN, ExceptionConstant.USER_AUTHENTICATION_REQUIRED.getMessage(),
                null);
        log.warn(LoggerConstant.USER_UNAUTHORIZED_REQUEST.getMessage(), request.getRequestURI());
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));
    }
}