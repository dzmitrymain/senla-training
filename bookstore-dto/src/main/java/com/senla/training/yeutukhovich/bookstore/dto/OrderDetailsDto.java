package com.senla.training.yeutukhovich.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
