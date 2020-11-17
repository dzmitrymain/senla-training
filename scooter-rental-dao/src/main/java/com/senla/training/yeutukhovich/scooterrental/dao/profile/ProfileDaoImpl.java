package com.senla.training.yeutukhovich.scooterrental.dao.profile;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDaoImpl extends AbstractDao<Profile, Long> implements ProfileDao {

    public ProfileDaoImpl() {
        super(Profile.class);
    }
}
