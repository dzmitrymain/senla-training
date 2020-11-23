package com.senla.training.yeutukhovich.scooterrental.dao.pass;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;

import java.util.List;

public interface PassDao extends GenericDao<Pass, Long> {

    List<Pass> findAllActivePasses();
}
