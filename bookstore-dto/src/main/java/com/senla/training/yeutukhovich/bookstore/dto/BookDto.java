package com.senla.training.yeutukhovich.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;

public class BookDto {

    private Long id;
    private String title;
    private Boolean isAvailable;
    private Integer editionYear;
    @JsonSerialize(using = DateConverter.Serialize.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date replenishmentDate;
    private BigDecimal price;

    public BookDto() {

    }

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isAvailable = book.isAvailable();
        this.editionYear = book.getEditionYear();
        this.replenishmentDate = book.getReplenishmentDate();
        this.price = book.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Integer getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(Integer editionYear) {
        this.editionYear = editionYear;
    }

    public Date getReplenishmentDate() {
        return replenishmentDate;
    }

    public void setReplenishmentDate(Date replenishmentDate) {
        this.replenishmentDate = replenishmentDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book [id=" + id +
                ", title='" + title +
                "', is available=" + isAvailable +
                ", price=" + price + "]";
    }
}
