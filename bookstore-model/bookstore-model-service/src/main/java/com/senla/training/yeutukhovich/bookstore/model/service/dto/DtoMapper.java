package com.senla.training.yeutukhovich.bookstore.model.service.dto;

import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;

public class DtoMapper {

    public static BookDto mapBook(Book book) {
        if (book == null) {
            return null;
        }
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAvailable(book.isAvailable());
        bookDto.setEditionYear(book.getEditionYear());
        bookDto.setReplenishmentDate(book.getReplenishmentDate());
        bookDto.setPrice(book.getPrice());
        return bookDto;
    }

    public static OrderDto mapOrder(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setState(order.getState().toString());
        orderDto.setBookDto(mapBook(order.getBook()));
        orderDto.setPrice(order.getCurrentBookPrice());
        orderDto.setCreationDate(order.getCreationDate());
        orderDto.setCompletionDate(order.getCompletionDate());
        orderDto.setCustomerData(order.getCustomerData());
        return orderDto;
    }

    public static RequestDto mapRequest(Request request) {
        if (request == null) {
            return null;
        }
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setBookDto(mapBook(request.getBook()));
        requestDto.setActive(request.isActive());
        requestDto.setRequesterData(request.getRequesterData());
        return requestDto;
    }
}
