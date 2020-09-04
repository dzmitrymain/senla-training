package com.senla.training.yeutukhovich.bookstore.dao.connector;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.PostConstruct;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DbConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbConnector.class);

    @ConfigProperty(propertyName = "database.path")
    private String url;
    @ConfigProperty(propertyName = "database.login")
    private String login;
    @ConfigProperty(propertyName = "database.password")
    private String password;
    @ConfigProperty(propertyName = "database.driverName")
    private String driverName;

    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection.isClosed()) {
                init();
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
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
