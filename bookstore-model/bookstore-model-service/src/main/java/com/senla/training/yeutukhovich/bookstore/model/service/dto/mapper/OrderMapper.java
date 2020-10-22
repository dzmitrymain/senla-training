package com.senla.training.yeutukhovich.bookstore.model.service.dto.mapper;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    @Autowired
    private BookMapper bookMapper;

    public OrderDto map(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setState(order.getState().toString());
        orderDto.setBookDto(bookMapper.map(order.getBook()));
        orderDto.setPrice(order.getCurrentBookPrice());
        orderDto.setCreationDate(order.getCreationDate());
        orderDto.setCompletionDate(order.getCompletionDate());
        orderDto.setCustomerData(order.getCustomerData());
        return orderDto;
    }

    public Order map(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setState(OrderState.valueOf(orderDto.getState()));
        order.setBook(bookMapper.map(orderDto.getBookDto()));
        order.setCurrentBookPrice(orderDto.getPrice());
        order.setCreationDate(orderDto.getCreationDate());
        order.setCompletionDate(orderDto.getCompletionDate());
        order.setCustomerData(orderDto.getCustomerData());
        return order;
    }
}
