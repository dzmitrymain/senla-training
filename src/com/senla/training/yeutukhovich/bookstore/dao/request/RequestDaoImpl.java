package com.senla.training.yeutukhovich.bookstore.dao.request;

import com.senla.training.yeutukhovich.bookstore.dao.AbstractEntityDao;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.Fields;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

@Singleton
public class RequestDaoImpl extends AbstractEntityDao<Request> implements RequestDao {

    private static final String FIND_ALL = "SELECT requests.id AS request_id, book_id, is_active, requester_data, title," +
            "is_available, edition_year, replenishment_date, price FROM requests JOIN books ON requests.book_id=books.id;";
    private static final String FIND_BY_ID = "SELECT requests.id AS request_id, book_id, is_active, requester_data, title, " +
            "is_available, edition_year, replenishment_date, price FROM requests JOIN books ON requests.book_id=books.id " +
            "WHERE requests.id=?";
    private static final String ADD_REQUEST = "INSERT INTO requests (book_id, is_active, requester_data) VALUES (?,?,?);";
    private static final String UPDATE_REQUEST = "UPDATE requests SET book_id=?, is_active=?, requester_data=? " +
            "WHERE id=?;";
    private static final String CLOSE_REQUESTS_BY_BOOK_ID = "UPDATE requests SET is_active=0 WHERE book_id=? AND is_active=1";

    @Override
    public Long closeRequestsByBookId(Connection connection, Long bookId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CLOSE_REQUESTS_BY_BOOK_ID)) {
            preparedStatement.setLong(1, bookId);
            return Long.valueOf(preparedStatement.executeUpdate());
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    protected String getFindAllQuery() {
        return FIND_ALL;
    }

    @Override
    protected String getFindByIdQuery() {
        return FIND_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return ADD_REQUEST;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_REQUEST;
    }

    @Override
    protected Request parseResultSetEntity(ResultSet resultSet) throws ParseException, SQLException {
        if (resultSet == null) {
            return null;
        }
        Request request = new Request();
        request.setId(resultSet.getLong(Fields.REQUEST_ID.getFieldName()));
        request.setActive(resultSet.getBoolean(Fields.IS_ACTIVE.getFieldName()));
        request.setRequesterData(resultSet.getString(Fields.REQUESTER_DATA.getFieldName()));
        Book book = new Book();
        book.setId(resultSet.getLong(Fields.BOOK_ID.getFieldName()));
        book.setTitle(resultSet.getString(Fields.TITLE.getFieldName()));
        book.setAvailable(resultSet.getBoolean(Fields.IS_AVAILABLE.getFieldName()));
        book.setEditionYear(resultSet.getInt(Fields.EDITION_YEAR.getFieldName()));
        book.setReplenishmentDate(new Date(resultSet.getTimestamp(Fields.REPLENISHMENT_DATE.getFieldName()).getTime()));
        book.setPrice(resultSet.getBigDecimal(Fields.PRICE.getFieldName()));
        request.setBook(book);
        return request;
    }

    @Override
    protected void prepareInsert(PreparedStatement preparedStatement, Request entity) throws SQLException {
        preparedStatement.setLong(1, entity.getBook().getId());
        preparedStatement.setBoolean(2, entity.isActive());
        preparedStatement.setString(3, entity.getRequesterData());
    }

    @Override
    protected void prepareUpdate(PreparedStatement preparedStatement, Request entity) throws SQLException {
        preparedStatement.setLong(1, entity.getBook().getId());
        preparedStatement.setBoolean(2, entity.isActive());
        preparedStatement.setString(3, entity.getRequesterData());
        preparedStatement.setLong(4, entity.getId());
    }
}
