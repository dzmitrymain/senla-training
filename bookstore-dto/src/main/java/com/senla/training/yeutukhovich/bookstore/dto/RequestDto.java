package com.senla.training.yeutukhovich.bookstore.dto;

import com.senla.training.yeutukhovich.bookstore.model.domain.Request;

public class RequestDto {

    private Long id;
    private BookDto bookDto;
    private Boolean isActive;
    private String requesterData;

    public RequestDto() {

    }

    public RequestDto(Request request) {
        this.id = request.getId();
        this.bookDto = new BookDto(request.getBook());
        this.isActive = request.isActive();
        this.requesterData = request.getRequesterData();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getRequesterData() {
        return requesterData;
    }

    public void setRequesterData(String requesterData) {
        this.requesterData = requesterData;
    }
}
