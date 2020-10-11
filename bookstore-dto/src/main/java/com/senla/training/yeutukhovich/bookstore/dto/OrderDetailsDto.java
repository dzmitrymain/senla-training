package com.senla.training.yeutukhovich.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailsDto {

    private String customerData;
    private String bookTitle;
    private String state;
    private BigDecimal price;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date creationDate;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date completionDate;

    public OrderDetailsDto() {

    }

    public OrderDetailsDto(String customerData, String bookTitle, String state, BigDecimal price, Date creationDate, Date completionDate) {
        this.customerData = customerData;
        this.bookTitle = bookTitle;
        this.state = state;
        this.price = price;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
    }

    public String getCustomerData() {
        return customerData;
    }

    public void setCustomerData(String customerData) {
        this.customerData = customerData;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    @Override
    public String toString() {
        StringBuilder completionDateToString = new StringBuilder();
        if (completionDate != null) {
            completionDateToString.append(", completion date=");
            completionDateToString.append(DateConverter.formatDate(completionDate, DateConverter.DAY_DATE_FORMAT));
        }
        return "Order details [" +
                "customer data='" + customerData +
                ", book title='" + bookTitle + '\'' +
                ", price='" + price + '\'' +
                ", state=" + state +
                ", creation date=" + DateConverter.formatDate(creationDate, DateConverter.DAY_DATE_FORMAT) +
                completionDateToString.toString() +
                ']';
    }
}
