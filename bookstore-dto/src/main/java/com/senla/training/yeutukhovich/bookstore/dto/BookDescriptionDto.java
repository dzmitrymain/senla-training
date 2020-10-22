package com.senla.training.yeutukhovich.bookstore.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDescriptionDto {

    private String title;
    private int editionYear;
    @JsonSerialize(using = DateConverter.Serializer.class)
    @JsonDeserialize(using = DateConverter.Deserializer.class)
    private Date replenishmentDate;
}
