package com.senla.training.yeutukhovich.bookstore.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.dto.ErrorDto;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GenericExceptionHandleFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        //обычно в фильтре исключения не ловят - есть же хэндлеры,
        // иначе лишнего можно поймать и испортить работу спринга
        // но в целом архитектура твоего приложения довольно логичная и удобная, хотя я чаще видел
        // контроллеры + секьюрити + веб = один модуль
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        // обычно ловят все же Exception - Error ловить бессмысленно
        } catch (Throwable e) {
            // мне кажется, ты ничего не потеряешь, если залогируешь log.error(e);
            log.error(e.getMessage(), e);
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
