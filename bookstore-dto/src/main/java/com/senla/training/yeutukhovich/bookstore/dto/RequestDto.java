package com.senla.training.yeutukhovich.bookstore.dto;

public class RequestDto {

    private Long id;
    private BookDto bookDto;
    private Boolean isActive;
    private String requesterData;

    public RequestDto() {

    }

    public RequestDto(Long id, BookDto bookDto, Boolean isActive, String requesterData) {
        this.id = id;
        this.bookDto = bookDto;
        this.isActive = isActive;
        this.requesterData = requesterData;
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

    @Override
    public String toString() {
        return "Request [id=" + id +
                ", is active=" + isActive +
                ", requester data='" + requesterData +
                "']";
    }
}
