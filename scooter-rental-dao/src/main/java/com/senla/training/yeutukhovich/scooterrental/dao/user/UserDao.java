package com.senla.training.yeutukhovich.scooterrental.dao.user;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findUserByUsername(String username);

    List<Pass> findAllActiveUserPasses(Long id);

    List<Rent> findAllSortedByCreationUserRents(Long id);
}
