package com.senla.training.yeutukhovich.scooterrental.service.rent;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rent.RentDao;
import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.type.PaymentType;
import com.senla.training.yeutukhovich.scooterrental.dto.CreationRentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.RateDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WithMockUser
class RentServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Rent RENT = Mockito.mock(Rent.class);
    private static final RentDto RENT_DTO = Mockito.mock(RentDto.class);
    private static final User USER = Mockito.mock(User.class);
    private static final UserDto USER_DTO = Mockito.mock(UserDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final Scooter SCOOTER = Mockito.mock(Scooter.class);
    private static final ScooterDto SCOOTER_DTO = Mockito.mock(ScooterDto.class);
    private static final Rate RATE = Mockito.mock(Rate.class);
    private static final RateDto RATE_DTO = Mockito.mock(RateDto.class);
    private static final CreationRentDto CREATION_RENT_DTO = Mockito.mock(CreationRentDto.class);

    @Autowired
    private RentService rentService;

    @Autowired
    private RentDao rentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ScooterDao scooterDao;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private RentDtoMapper rentDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Autowired
    private ScooterDtoMapper scooterDtoMapper;
    @Autowired
    private RateDtoMapper rateDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(RENT.getId()).thenReturn(ENTITY_ID);
        Mockito.when(RENT_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(RENT.getUser()).thenReturn(USER);
        Mockito.when(RENT.getScooter()).thenReturn(SCOOTER);
        Mockito.when(SCOOTER.getModel()).thenReturn(MODEL);
        Mockito.when(MODEL.getId()).thenReturn(ENTITY_ID);
        Mockito.when(USER.getUsername()).thenReturn("user");
        Mockito.when(CREATION_RENT_DTO.getUserId()).thenReturn(ENTITY_ID);
        Mockito.when(CREATION_RENT_DTO.getScooterId()).thenReturn(ENTITY_ID);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(rentDtoMapper.map(RENT)).thenReturn(RENT_DTO);
        Mockito.when(scooterDtoMapper.map(SCOOTER)).thenReturn(SCOOTER_DTO);
        Mockito.when(scooterDtoMapper.map(SCOOTER_DTO)).thenReturn(SCOOTER);
        Mockito.when(userDtoMapper.map(USER)).thenReturn(USER_DTO);
        Mockito.when(userDtoMapper.map(USER_DTO)).thenReturn(USER);
        Mockito.when(rateDtoMapper.map(RATE)).thenReturn(RATE_DTO);
        Mockito.clearInvocations(rentDao);
    }

    @Test
    void RentServiceImpl_findAllActiveRents() {
        rentService.findAllActiveRents();

        Mockito.verify(rentDao, Mockito.times(1)).findAllActiveRents();
    }

    @Test
    void RentServiceImpl_findTotalProfit() {
        rentService.findTotalProfit();

        Mockito.verify(rentDao, Mockito.times(1)).findTotalProfit();
    }

    @Test
    void RentServiceImpl_findExpiredActiveRents() {
        rentService.findExpiredActiveRents();

        Mockito.verify(rentDao, Mockito.times(1)).findAllExpiredActiveRents();
    }

    @Test
    void RentServiceImpl_findAll() {
        rentService.findAll();

        Mockito.verify(rentDao, Mockito.times(1)).findAll();
    }

    @Test
    void RentServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RENT));

        RentDto rentDto = rentService.findById(ENTITY_ID);

        Assertions.assertNotNull(rentDto);
    }

    @Test
    void RentServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rentService.findById(ENTITY_ID));
    }

    @Test
    void RentServiceImpl_deleteById() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RENT));

        RentDto rentDto = rentService.deleteById(ENTITY_ID);

        Mockito.verify(rentDao, Mockito.times(1)).delete(RENT);
        Assertions.assertNotNull(rentDto);
    }

    @Test
    void RentServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rentService.deleteById(ENTITY_ID));
    }

    @Test
    void RentServiceImpl_completeRent() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RENT));
        Mockito.when(Objects.requireNonNull(RENT).getActive()).thenReturn(true);
        Mockito.when(RENT.getExpiredDate()).thenReturn(LocalDateTime.MAX);
        Mockito.when(rentDao.update(RENT)).thenReturn(RENT);

        RentDto rentDto = rentService.completeRent(ENTITY_ID, 0);

        Mockito.verify(rentDao, Mockito.times(1)).update(RENT);
        Assertions.assertNotNull(rentDto);
    }

    @Test
    void RentServiceImpl_completeRent_shouldThrowBusinessExceptionIfRentNotExist() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rentService.completeRent(ENTITY_ID, 0));
    }

    @Test
    void RentServiceImpl_completeRent_shouldThrowBusinessExceptionIfRentNotActive() {
        Mockito.when(rentDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RENT));
        Mockito.when(Objects.requireNonNull(RENT).getActive()).thenReturn(false);

        Assertions.assertThrows(BusinessException.class, () -> rentService.completeRent(ENTITY_ID, 0));
    }

    @Test
    void RentServiceImpl_create() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));
        Mockito.when(scooterDao.findActiveRentScooters()).thenReturn(new ArrayList<>());
        Mockito.when(CREATION_RENT_DTO.getExpiredDate()).thenReturn(LocalDateTime.MAX);
        Mockito.when(CREATION_RENT_DTO.getPaymentType()).thenReturn(PaymentType.RATE.name());
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));
        Mockito.when(RATE_DTO.getPerHour()).thenReturn(BigDecimal.ONE);
        Mockito.when(RATE_DTO.getWeekendPerHour()).thenReturn(BigDecimal.ONE);
        Mockito.when(rentDtoMapper.map(Mockito.any(Rent.class))).thenReturn(RENT_DTO);

        RentDto rentDto = rentService.create(CREATION_RENT_DTO);

        Mockito.verify(rentDao, Mockito.times(1)).add(Mockito.any(Rent.class));
        Assertions.assertNotNull(rentDto);
    }

    @Test
    void RentServiceImpl_create_shouldThrowBusinessExceptionIfScooterNotExist() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rentService.create(CREATION_RENT_DTO));
    }

    @Test
    void RentServiceImpl_create_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));

        Assertions.assertThrows(BusinessException.class, () -> rentService.create(CREATION_RENT_DTO));
    }

    @Test
    void RentServiceImpl_create_shouldThrowBusinessExceptionIfScooterNotAvailable() {
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));
        Mockito.when(scooterDao.findActiveRentScooters()).thenReturn(List.of(Objects.requireNonNull(SCOOTER)));

        Assertions.assertThrows(BusinessException.class, () -> rentService.create(CREATION_RENT_DTO));
    }
}
