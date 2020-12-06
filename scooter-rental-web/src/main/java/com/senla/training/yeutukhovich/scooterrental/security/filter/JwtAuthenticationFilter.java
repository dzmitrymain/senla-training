package com.senla.training.yeutukhovich.scooterrental.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.scooterrental.dto.AuthenticationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.AuthenticationResponseDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ErrorDto;
import com.senla.training.yeutukhovich.scooterrental.security.provider.JwtTokenProvider;
import com.senla.training.yeutukhovich.scooterrental.security.util.SecurityConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String AUTH_ENDPOINT = "/users/auth";

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenProvider tokenProvider,
                                   ObjectMapper objectMapper) {
        super(authenticationManager);
        setFilterProcessesUrl(AUTH_ENDPOINT);
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        AuthenticationRequestDto requestDto = objectMapper.readValue(
                request.getInputStream(), AuthenticationRequestDto.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        log.info(LoggerConstant.USER_AUTH_FAILURE.getMessage());
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED, ExceptionConstant.USER_AUTHORIZATION_FAIL.getMessage(),
                null);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        log.info(LoggerConstant.USER_AUTH_SUCCESS.getMessage(), authResult.getName());
        String token = tokenProvider.generateToken(authResult);
        response.addHeader(SecurityConstant.HEADER_STRING.getConstant(),
                SecurityConstant.TOKEN_PREFIX.getConstant() + token);
        AuthenticationResponseDto responseDto = new AuthenticationResponseDto(HttpStatus.OK,
                SecurityConstant.TOKEN_PREFIX.getConstant() + token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}
