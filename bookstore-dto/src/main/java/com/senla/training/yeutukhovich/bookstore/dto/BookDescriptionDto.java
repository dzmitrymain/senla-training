package com.senla.training.yeutukhovich.bookstore.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.util.Date;

public class BookDescriptionDto {

    private String title;
    private int editionYear;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date replenishmentDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(int editionYear) {
        this.editionYear = editionYear;
    }

    public Date getReplenishmentDate() {
        return replenishmentDate;
    }

    public void setReplenishmentDate(Date replenishmentDate) {
        this.replenishmentDate = replenishmentDate;
    }

    @Override
    public String toString() {
        StringBuilder replenishmentDateToString = new StringBuilder();
        if (replenishmentDate != null) {
            replenishmentDateToString.append(", replenishment date=");
            replenishmentDateToString.append(DateConverter.formatDate(replenishmentDate, DateConverter.DAY_DATE_FORMAT));
        }

        return "Book description [" +
                "title='" + title +
                "', edition year=" + editionYear +
                replenishmentDateToString.toString() + ']';
    }
}
