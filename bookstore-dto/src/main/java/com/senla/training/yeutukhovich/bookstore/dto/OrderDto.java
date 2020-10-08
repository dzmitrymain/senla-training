package com.senla.training.yeutukhovich.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDto {

    private Long id;
    private String state;
    private BookDto bookDto;
    private BigDecimal price;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date creationDate;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date completionDate;
    private String customerData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getCustomerData() {
        return customerData;
    }

    public void setCustomerData(String customerData) {
        this.customerData = customerData;
    }

    @Override
    public String toString() {
        return "Order [id=" + id +
                ", state=" + state +
                ", customer data='" + customerData + "']";
    }
}
