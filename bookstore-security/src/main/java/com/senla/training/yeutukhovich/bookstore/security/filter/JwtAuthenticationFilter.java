package com.senla.training.yeutukhovich.bookstore.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.dto.ErrorDto;
import com.senla.training.yeutukhovich.bookstore.dto.auth.AuthenticationRequestDto;
import com.senla.training.yeutukhovich.bookstore.dto.auth.AuthenticationResponseDto;
import com.senla.training.yeutukhovich.bookstore.security.provider.JwtTokenProvider;
import com.senla.training.yeutukhovich.bookstore.security.util.SecurityConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String AUTH_ENDPOINT = "/auth";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(ApplicationContext applicationContext) {
        this.authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        this.tokenProvider = applicationContext.getBean(JwtTokenProvider.class);
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
        setFilterProcessesUrl(AUTH_ENDPOINT);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequestDto requestDto = objectMapper.readValue(
                    request.getInputStream(), AuthenticationRequestDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.UNAUTHORIZED);
        errorDto.setMessage(MessageConstant.AUTHORIZATION_FAIL.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String token = tokenProvider.generateToken(authResult);
        response.addHeader(SecurityConstant.HEADER_STRING.getConstant(),
                SecurityConstant.TOKEN_PREFIX.getConstant() + token);
        AuthenticationResponseDto responseDto = new AuthenticationResponseDto();
        responseDto.setStatus(HttpStatus.OK);
        responseDto.setToken(SecurityConstant.TOKEN_PREFIX.getConstant() + token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}
