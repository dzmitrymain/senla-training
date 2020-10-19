package com.senla.training.yeutukhovich.bookstore.model.service.dto.mapper;

import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    @Autowired
    private BookMapper bookMapper;

    public RequestDto map(Request request) {
        if (request == null) {
            return null;
        }
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setBookDto(bookMapper.map(request.getBook()));
        requestDto.setActive(request.getActive());
        requestDto.setRequesterData(request.getRequesterData());
        return requestDto;
    }
}
