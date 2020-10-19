package com.senla.training.yeutukhovich.bookstore.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.dto.ErrorDto;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenericExceptionHandleFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionHandleFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ErrorDto errorDto = new ErrorDto();
            errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorDto.setMessage(MessageConstant.SOMETHING_WENT_WRONG.getMessage());
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorDto));
        }
    }
}
