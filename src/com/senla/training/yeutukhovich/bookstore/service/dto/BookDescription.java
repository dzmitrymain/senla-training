package com.senla.training.yeutukhovich.bookstore.service.dto;


import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.util.Date;

public class BookDescription {

    private String title;
    private Date editionDate;
    private Date replenishmentDate;

    public BookDescription() {

    }

    public BookDescription(String title, Date editionDate, Date replenishmentDate) {
        this.title = title;
        this.editionDate = editionDate;
        this.replenishmentDate = replenishmentDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
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
            replenishmentDateToString.append(", replenishmentDate=");
            replenishmentDateToString.append(DateConverter.formatDate(replenishmentDate, DateConverter.DAY_DATE_FORMAT));
        }

        return "Book description [" +
                "title='" + title +
                "', editionDate=" + DateConverter.formatDate(editionDate, DateConverter.YEAR_DATE_FORMAT) +
                replenishmentDateToString.toString() + ']';
    }
}
