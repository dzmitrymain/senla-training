package com.senla.training.yeutukhovich.scooterrental.dao.review;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDaoImpl extends AbstractDao<Review, Long> implements ReviewDao {

    public ReviewDaoImpl() {
        super(Review.class);
    }
}
