package com.senla.training.yeutukhovich.scooterrental.service.rent;

import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rent.RentDao;
import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.type.PaymentType;
import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.StartRentDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.UserDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.scooter.ScooterService;
import com.senla.training.yeutukhovich.scooterrental.service.user.UserService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RentServiceImpl implements RentService {

    private static final String ENTITY_NAME = "Rent";

    private final RentDao rentDao;
    private final ScooterDao scooterDao;
    private final ModelDao modelDao;
    private final PassDao passDao;
    private final UserDao userDao;

    private final RentDtoMapper rentDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final ScooterDtoMapper scooterDtoMapper;

    private final UserService userService;
    private final ScooterService scooterService;

    //TODO: properties
    private BigDecimal overtimeCoefficient = BigDecimal.valueOf(1.5);

    @Autowired
    public RentServiceImpl(RentDao rentDao,
                           ScooterDao scooterDao,
                           ModelDao modelDao,
                           PassDao passDao,
                           UserDao userDao,
                           RentDtoMapper rentDtoMapper,
                           UserDtoMapper userDtoMapper,
                           ScooterDtoMapper scooterDtoMapper,
                           UserService userService,
                           ScooterService scooterService) {
        this.rentDao = rentDao;
        this.scooterDao = scooterDao;
        this.modelDao = modelDao;
        this.passDao = passDao;
        this.userDao = userDao;
        this.rentDtoMapper = rentDtoMapper;
        this.userDtoMapper = userDtoMapper;
        this.scooterDtoMapper = scooterDtoMapper;
        this.userService = userService;
        this.scooterService = scooterService;
    }

    @Override
    @Transactional
    public List<RentDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return rentDao.findAll().stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RentDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return rentDtoMapper.map(findRentById(id));
    }

    @Override
    @Transactional
    public RentDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Rent rentToDelete = findRentById(id);
        rentDao.delete(rentToDelete);
        return rentDtoMapper.map(rentToDelete);
    }

    @Override
    @Transactional
    public RentDto completeRent(Long id, Integer distanceTravelled) {
        log.info(LoggerConstant.RENT_END.getMessage(), id);
        Rent rent = findRentById(id);
        if (!rent.getActive()) {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.RENT_ALREADY_ENDS.getMessage(), id), HttpStatus.FORBIDDEN);
            log.warn(exception.getMessage());
            throw exception;
        }
        final LocalDateTime currentDateTime = LocalDateTime.now();
        rent.setActive(false);
        rent.setReturnDate(currentDateTime);
        rent.setDistanceTravelled(distanceTravelled);
        rent.setOvertimePenalty(BigDecimal.ZERO);

        Duration duration = Duration.between(currentDateTime, rent.getExpiredDate());
        long timeSubtractionMinutes = Math.abs(duration.toMinutes());
        if (currentDateTime.isBefore(rent.getExpiredDate())) {
            Long bonusPassId = createBonusPass(rent, currentDateTime, timeSubtractionMinutes);
            log.info(LoggerConstant.RENT_PASS_CREATED.getMessage(), bonusPassId, rent.getUser().getId(),
                    rent.getScooter().getModel().getId(), (int) timeSubtractionMinutes);
        } else if (currentDateTime.isAfter(rent.getExpiredDate())) {
            rent.setOvertimePenalty(overtimeCoefficient.multiply(calculateRentPrice(rent.getScooter().getModel(),
                    currentDateTime, timeSubtractionMinutes)).setScale(2, RoundingMode.HALF_EVEN));
        }
        rent = rentDao.update(rent);
        log.info(LoggerConstant.RENT_END_SUCCESS.getMessage(), rent.getId(), rent.getOvertimePenalty());
        return rentDtoMapper.map(rent);
    }

    @Override
    @Transactional
    public RentDto create(StartRentDto startRentDto) {
        log.info(LoggerConstant.RENT_START.getMessage(), startRentDto.getUserId(), startRentDto.getScooterId(),
                startRentDto.getPaymentType());
        Rent rent = new Rent();
        rent.setUser(userDtoMapper.map(userService.findById(startRentDto.getUserId())));
        rent.setScooter(scooterDtoMapper.map(scooterService.findById(startRentDto.getScooterId())));
        List<Rent> rents;
        if (!(rents = scooterDao.findSortedByCreationScooterRents(rent.getScooter().getId())).isEmpty() &&
                rents.get(0).getActive()) {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.SCOOTER_NOT_AVAILABLE.getMessage(), startRentDto.getScooterId()),
                    HttpStatus.FORBIDDEN);
            log.warn(exception.getMessage());
            throw exception;
        }
        rent.setExpiredDate(startRentDto.getExpiredDate());
        rent.setPaymentType(PaymentType.valueOf(startRentDto.getPaymentType()));
        final LocalDateTime currentDateTime = LocalDateTime.now();
        rent.setCreationDate(currentDateTime);
        rent.setPrice(completePayment(startRentDto, rent.getScooter().getModel(), currentDateTime));
        rent.setActive(true);
        rentDao.add(rent);
        log.info(LoggerConstant.RENT_START_SUCCESS.getMessage(), rent.getId(), rent.getUser().getId(), rent.getScooter().getId());
        return rentDtoMapper.map(rent);
    }

    @Override
    @Transactional
    public List<RentDto> findAllActiveRents() {
        log.info(LoggerConstant.RENTS_ACTIVE.getMessage());
        return rentDao.findAllActiveRents().stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BigDecimal findTotalProfit() {
        log.info(LoggerConstant.RENTS_TOTAL_PROFIT.getMessage());
        return rentDao.findTotalProfit();
    }

    @Override
    @Transactional
    public List<RentDto> findExpiredActiveRents() {
        log.info(LoggerConstant.RENTS_EXPIRED_ACTIVE.getMessage());
        return rentDao.findAllExpiredActiveRents().stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Rent findRentById(Long rentId) {
        return rentDao.findById(rentId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }

    private BigDecimal completePayment(StartRentDto startRentDto, Model model, LocalDateTime currentDateTime) {
        Duration rentDuration = Duration.between(currentDateTime, startRentDto.getExpiredDate());
        long rentDurationInMinutesToPay = rentDuration.toMinutes();

        if (startRentDto.getPaymentType().equals(PaymentType.PASS.name())) {
            List<Pass> activeUserPasses = userDao.findAllActiveUserPasses(startRentDto.getUserId()).stream()
                    .filter(pass -> pass.getModel().getId().equals(model.getId()))
                    .collect(Collectors.toList());
            for (Pass activeUserPass : activeUserPasses) {
                if (activeUserPass.getRemainingMinutes() >= rentDurationInMinutesToPay) {
                    activeUserPass.setRemainingMinutes(activeUserPass.getRemainingMinutes() - (int) rentDurationInMinutesToPay);
                    rentDurationInMinutesToPay = 0;
                    passDao.update(activeUserPass);
                    break;
                } else {
                    rentDurationInMinutesToPay = rentDurationInMinutesToPay - activeUserPass.getRemainingMinutes();
                    activeUserPass.setRemainingMinutes(0);
                    passDao.update(activeUserPass);
                }
            }
        }
        if (rentDurationInMinutesToPay <= 0) {
            return BigDecimal.ZERO;
        }
        return calculateRentPrice(model, currentDateTime, rentDurationInMinutesToPay);
    }

    private BigDecimal calculateRentPrice(Model model, LocalDateTime currentDateTime, long rentDurationInMinutesToPay) {
        Rate rate = modelDao.findCurrentRateByModelId(model.getId());
        final BigDecimal currentRate;
        if (currentDateTime.getDayOfWeek() == DayOfWeek.SATURDAY || currentDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            currentRate = rate.getWeekendPerHour();
        } else {
            currentRate = rate.getPerHour();
        }
        long hoursToPay = rentDurationInMinutesToPay / 60;
        if (rentDurationInMinutesToPay % 60 != 0) {
            hoursToPay++;
        }
        return currentRate.multiply(BigDecimal.valueOf(hoursToPay)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private Long createBonusPass(Rent rent, LocalDateTime currentDateTime, long timeSubtractionMinutes) {
        Pass bonusPass = new Pass();
        bonusPass.setUser(rent.getUser());
        bonusPass.setModel(rent.getScooter().getModel());
        bonusPass.setTotalMinutes((int) timeSubtractionMinutes);
        bonusPass.setPrice(BigDecimal.ZERO);
        bonusPass.setCreationDate(currentDateTime);
        bonusPass.setExpiredDate(currentDateTime.plusMonths(1));
        return passDao.add(bonusPass);
    }
}
