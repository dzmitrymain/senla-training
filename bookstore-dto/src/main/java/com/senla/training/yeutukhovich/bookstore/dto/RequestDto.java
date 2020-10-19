package com.senla.training.yeutukhovich.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;
    private BookDto bookDto;
    private Boolean active;
    private String requesterData;
}
