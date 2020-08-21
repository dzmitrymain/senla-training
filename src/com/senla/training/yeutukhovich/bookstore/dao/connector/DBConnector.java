package com.senla.training.yeutukhovich.bookstore.dao.connector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.PostConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DBConnector {

    @ConfigProperty(propertyName = "database.path")
    private String url;
    @ConfigProperty(propertyName = "database.login")
    private String login;
    @ConfigProperty(propertyName = "database.password")
    private String password;

    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InternalException(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
