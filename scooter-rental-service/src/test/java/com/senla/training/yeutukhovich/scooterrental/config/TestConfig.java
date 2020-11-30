package com.senla.training.yeutukhovich.scooterrental.config;

import com.senla.training.yeutukhovich.scooterrental.dao.discount.DiscountDao;
import com.senla.training.yeutukhovich.scooterrental.dao.location.LocationDao;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.dao.profile.ProfileDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rate.RateDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rent.RentDao;
import com.senla.training.yeutukhovich.scooterrental.dao.review.ReviewDao;
import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.dao.spot.SpotDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.mapper.DiscountDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.LocationDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ModelDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.PassDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.RateDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ReviewDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.SpotDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("com.senla.training.yeutukhovich.scooterrental.service")
public class TestConfig {

    @Bean
    public DiscountDao discountDao() {
        return Mockito.mock(DiscountDao.class);
    }

    @Bean
    public DiscountDtoMapper discountDtoMapper() {
        return Mockito.mock(DiscountDtoMapper.class);
    }

    @Bean
    public LocationDao locationDao() {
        return Mockito.mock(LocationDao.class);
    }

    @Bean
    public LocationDtoMapper locationDtoMapper() {
        return Mockito.mock(LocationDtoMapper.class);
    }

    @Bean
    public ModelDao modelDao() {
        return Mockito.mock(ModelDao.class);
    }

    @Bean
    public ModelDtoMapper modelDtoMapper() {
        return Mockito.mock(ModelDtoMapper.class);
    }

    @Bean
    public PassDao passDao() {
        return Mockito.mock(PassDao.class);
    }

    @Bean
    public PassDtoMapper passDtoMapper() {
        return Mockito.mock(PassDtoMapper.class);
    }

    @Bean
    public ProfileDao profileDao() {
        return Mockito.mock(ProfileDao.class);
    }

    @Bean
    public ProfileDtoMapper profileDtoMapper() {
        return Mockito.mock(ProfileDtoMapper.class);
    }

    @Bean
    public RentDao rentDao() {
        return Mockito.mock(RentDao.class);
    }

    @Bean
    public RentDtoMapper rentDtoMapper() {
        return Mockito.mock(RentDtoMapper.class);
    }

    @Bean
    public ScooterDao scooterDao() {
        return Mockito.mock(ScooterDao.class);
    }

    @Bean
    public ScooterDtoMapper scooterDtoMapper() {
        return Mockito.mock(ScooterDtoMapper.class);
    }

    @Bean
    public ReviewDao reviewDao() {
        return Mockito.mock(ReviewDao.class);
    }

    @Bean
    public ReviewDtoMapper reviewDtoMapper() {
        return Mockito.mock(ReviewDtoMapper.class);
    }

    @Bean
    public RateDao rateDao() {
        return Mockito.mock(RateDao.class);
    }

    @Bean
    public RateDtoMapper rateDtoMapper() {
        return Mockito.mock(RateDtoMapper.class);
    }

    @Bean
    public SpotDao spotDao() {
        return Mockito.mock(SpotDao.class);
    }

    @Bean
    public SpotDtoMapper spotDtoMapper() {
        return Mockito.mock(SpotDtoMapper.class);
    }

    @Bean
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    public UserDtoMapper userDtoMapper() {
        return Mockito.mock(UserDtoMapper.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return Mockito.mock(GeometryFactory.class);
    }
}
