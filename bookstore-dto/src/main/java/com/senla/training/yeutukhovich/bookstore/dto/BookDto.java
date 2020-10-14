package com.senla.training.yeutukhovich.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class BookDto {

    private Long id;
    private String title;
    private Boolean isAvailable;
    private Integer editionYear;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date replenishmentDate;
    private BigDecimal price;

    public BookDto() {

    }

    public BookDto(Long id, String title, Boolean isAvailable, Integer editionYear,
                   Date replenishmentDate, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
        this.editionYear = editionYear;
        this.replenishmentDate = replenishmentDate;
        this.price = price;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookDto bookDto = (BookDto) o;

        if (!id.equals(bookDto.id)) return false;
        if (!title.equals(bookDto.title)) return false;
        if (!isAvailable.equals(bookDto.isAvailable)) return false;
        if (!editionYear.equals(bookDto.editionYear)) return false;
        if (!Objects.equals(replenishmentDate, bookDto.replenishmentDate))
            return false;
        return price.equals(bookDto.price);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + isAvailable.hashCode();
        result = 31 * result + editionYear.hashCode();
        result = 31 * result + (replenishmentDate != null ? replenishmentDate.hashCode() : 0);
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book [id=" + id +
                ", title='" + title +
                "', is available=" + isAvailable +
                ", price=" + price + "]";
    }
}
