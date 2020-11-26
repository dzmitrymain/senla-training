package com.senla.training.yeutukhovich.scooterrental.dao.review;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;

import java.math.BigDecimal;

public interface ReviewDao extends GenericDao<Review, Long> {

    Double findAverageScore();
}
