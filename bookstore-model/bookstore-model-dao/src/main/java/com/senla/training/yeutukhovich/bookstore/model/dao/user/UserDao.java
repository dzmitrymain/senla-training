package com.senla.training.yeutukhovich.bookstore.model.dao.user;

import com.senla.training.yeutukhovich.bookstore.model.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findUserByUserName(String username);
}
