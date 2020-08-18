package com.senla.training.yeutukhovich.bookstore.util.converter;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.util.constant.Fields;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public class DBResultSetParser {

    private DBResultSetParser() {
    }

    public static Book parseBook(ResultSet resultSet) throws SQLException, ParseException {
        if (resultSet == null) {
            return null;
        }
        Book book = new Book();
        book.setId(resultSet.getLong(Fields.BOOK_ID.getFieldName()));
        book.setTitle(resultSet.getString(Fields.TITLE.getFieldName()));
        book.setAvailable(resultSet.getBoolean(Fields.IS_AVAILABLE.getFieldName()));
        book.setEditionDate(DateConverter.parseDate(String.valueOf(resultSet.getInt(Fields.EDITION_DATE.getFieldName())), DateConverter.YEAR_DATE_FORMAT));
        book.setReplenishmentDate(new Date(resultSet.getTimestamp(Fields.REPLENISHMENT_DATE.getFieldName()).getTime()));
        book.setPrice(resultSet.getBigDecimal(Fields.PRICE.getFieldName()));
        return book;
    }

    public static Order parseOrder(ResultSet resultSet) throws SQLException, ParseException {
        if (resultSet == null) {
            return null;
        }
        Order order = new Order();
        order.setId(resultSet.getLong(Fields.ORDER_ID.getFieldName()));
        order.setState(OrderState.valueOf(resultSet.getString(Fields.STATE_TYPE.getFieldName())));
        order.setCurrentBookPrice(resultSet.getBigDecimal(Fields.CURRENT_PRICE.getFieldName()));
        order.setCreationDate(new Date(resultSet.getTimestamp(Fields.CREATION_DATE.getFieldName()).getTime()));
        Optional.ofNullable(resultSet.getTimestamp(Fields.COMPLETION_DATE.getFieldName()))
                .ifPresent(timestamp -> order.setCompletionDate(new Date(timestamp.getTime())));
        order.setCustomerData(resultSet.getString(Fields.CUSTOMER_DATA.getFieldName()));
        order.setBook(parseBook(resultSet));
        return order;
    }

    public static Request parseRequest(ResultSet resultSet) throws SQLException, ParseException {
        if (resultSet == null) {
            return null;
        }
        Request request = new Request();
        request.setId(resultSet.getLong(Fields.REQUEST_ID.getFieldName()));
        request.setActive(resultSet.getBoolean(Fields.IS_ACTIVE.getFieldName()));
        request.setRequesterData(resultSet.getString(Fields.REQUESTER_DATA.getFieldName()));
        request.setBook(parseBook(resultSet));
        return request;
    }
}
