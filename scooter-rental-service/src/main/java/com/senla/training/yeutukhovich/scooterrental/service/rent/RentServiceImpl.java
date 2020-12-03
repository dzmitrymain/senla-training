package com.senla.training.yeutukhovich.scooterrental.service.rent;

import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rent.RentDao;
import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.type.PaymentType;
import com.senla.training.yeutukhovich.scooterrental.dto.CreationRentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.model.ModelService;
import com.senla.training.yeutukhovich.scooterrental.service.scooter.ScooterService;
import com.senla.training.yeutukhovich.scooterrental.service.user.UserService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class RentServiceImpl implements RentService {

    private static final String ENTITY_NAME = "Rent";

    private final RentDao rentDao;
    private final ScooterDao scooterDao;
    private final PassDao passDao;
    private final UserDao userDao;

    private final RentDtoMapper rentDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final ScooterDtoMapper scooterDtoMapper;

    private final ModelService modelService;
    private final UserService userService;
    private final ScooterService scooterService;

    @Value("${RentServiceImpl.overtimeCoefficient:1.5}")
    private BigDecimal overtimeCoefficient;

    @Autowired
    public RentServiceImpl(RentDao rentDao,
                           ScooterDao scooterDao,
                           PassDao passDao,
                           UserDao userDao,
                           RentDtoMapper rentDtoMapper,
                           UserDtoMapper userDtoMapper,
                           ScooterDtoMapper scooterDtoMapper,
                           ModelService modelService,
                           UserService userService,
                           ScooterService scooterService) {
        this.rentDao = rentDao;
        this.scooterDao = scooterDao;
        this.passDao = passDao;
        this.userDao = userDao;
        this.rentDtoMapper = rentDtoMapper;
        this.userDtoMapper = userDtoMapper;
        this.scooterDtoMapper = scooterDtoMapper;
        this.modelService = modelService;
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

    /**
     * This method ends the {@link Rent} and apply changes in the database. And returns updated {@link Rent} mapped to
     * {@link RentDto}.
     * <p>
     * It invokes {@link #findById(Long)} to find the current {@link Rent}.
     * Then invokes {@link #checkUserMatch(User)} in  which it checks that the current {@link User} matches the one
     * specified in the {@link Rent}.
     * If {@link Rent} is expired then invokes {@link #calculateRentPrice(Model, long)} and the returned value is
     * multiplied by a {@link #overtimeCoefficient} to get {@link Rent#getOvertimePenalty()}.
     * If {@link Rent} is not expired then invokes {@link #createBonusPass(Rent, LocalDateTime, long)} and a bonus
     * {@link Pass} is created and added to database, with unused minutes.
     *
     * @param id                of the {@link User} who ends the {@link Rent}
     * @param distanceTravelled distance traveled by the {@link User} on the {@link Scooter}
     * @return RentDto mapped from modified Rent
     * @throws BusinessException     if Rent doesn't exist or Rent is already not active.
     * @throws AccessDeniedException if the current User does not match the specified one
     */
    @Override
    @Transactional
    public RentDto completeRent(Long id, @PositiveOrZero Integer distanceTravelled) {
        log.info(LoggerConstant.RENT_END.getMessage(), id);
        Rent rent = findRentById(id);
        checkUserMatch(rent.getUser());
        if (!rent.getActive()) {
            throw new BusinessException(
                    String.format(ExceptionConstant.RENT_ALREADY_ENDS.getMessage(), id), HttpStatus.FORBIDDEN);
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
                    timeSubtractionMinutes)).setScale(2, RoundingMode.HALF_EVEN));
        }
        rent = rentDao.update(rent);
        log.info(LoggerConstant.RENT_END_SUCCESS.getMessage(), rent.getId(), rent.getOvertimePenalty());
        return rentDtoMapper.map(rent);
    }

    /**
     * This method creates and adds a {@link Rent} object to the database. And returns the created object mapped
     * to {@link RentDto}.
     * <p>
     * It invokes {@link UserService#findById(Long)} to find the {@link User}.
     * Then invokes {@link #checkUserMatch(User)} in  which it checks that the current {@link User} matches the one
     * specified in the {@link CreationRentDto}.
     * Then invokes {@link ScooterService#findById(Long)} to find the {@link Scooter}.
     * Then invokes {@link ScooterDao#findActiveRentScooters()} to check that {@link Scooter} is available.
     * And invokes {@link #completePayment(CreationRentDto, Model, LocalDateTime)} in which, depending on
     * the {@link CreationRentDto#getPaymentType()}, deducts the remaining minutes from the {@link User}'s {@link Pass}es,
     * and calculates the {@link Rent} price according to
     * the {@link Rate}.
     *
     * @param creationRentDto an object that contains information about User, rental Scooter, rental time, PaymentType
     * @return RentDto mapped from created Rent
     * @throws BusinessException     if User doesn't exist or Scooter doesn't exist or Scooter isn't available
     * @throws AccessDeniedException if the current User does not match the specified one
     */
    @Override
    @Transactional
    public RentDto create(@Valid CreationRentDto creationRentDto) {
        log.info(LoggerConstant.RENT_START.getMessage(), creationRentDto.getUserId(), creationRentDto.getScooterId(),
                creationRentDto.getPaymentType());
        Rent rent = new Rent();
        rent.setUser(userDtoMapper.map(userService.findById(creationRentDto.getUserId())));
        checkUserMatch(rent.getUser());
        rent.setScooter(scooterDtoMapper.map(scooterService.findById(creationRentDto.getScooterId())));
        if (scooterDao.findActiveRentScooters().contains(rent.getScooter())) {
            throw new BusinessException(
                    String.format(ExceptionConstant.SCOOTER_NOT_AVAILABLE.getMessage(), creationRentDto.getScooterId()),
                    HttpStatus.FORBIDDEN);
        }
        rent.setExpiredDate(creationRentDto.getExpiredDate());
        rent.setPaymentType(PaymentType.valueOf(creationRentDto.getPaymentType().toUpperCase()));
        final LocalDateTime currentDateTime = LocalDateTime.now();
        rent.setCreationDate(currentDateTime);
        rent.setPrice(completePayment(creationRentDto, rent.getScooter().getModel(), currentDateTime));
        rent.setActive(true);
        rentDao.add(rent);
        log.info(LoggerConstant.RENT_START_SUCCESS.getMessage(), rent.getId(), rent.getUser().getId(),
                rent.getScooter().getId());
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
        return rentDao.findById(rentId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }

    private void checkUserMatch(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !user.getUsername().equals(auth.getName())) {
            throw new AccessDeniedException(ExceptionConstant.USER_ACCESS_DENIED.getMessage());
        }
    }

    private BigDecimal completePayment(CreationRentDto creationRentDto, Model model, LocalDateTime currentDateTime) {
        Duration rentDuration = Duration.between(currentDateTime, creationRentDto.getExpiredDate());
        long rentDurationInMinutesToPay = rentDuration.toMinutes();

        if (PaymentType.PASS.name().equals(creationRentDto.getPaymentType())) {
            List<Pass> activeUserPasses = userDao.findAllActiveUserPasses(creationRentDto.getUserId()).stream()
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
        return calculateRentPrice(model, rentDurationInMinutesToPay);
    }

    private BigDecimal calculateRentPrice(Model model, long rentDurationInMinutesToPay) {
        BigDecimal currentPerHour = modelService.findCurrentModelPrice(model.getId());
        long hoursToPay = rentDurationInMinutesToPay / 60;
        if (rentDurationInMinutesToPay % 60 != 0) {
            hoursToPay++;
        }
        return currentPerHour.multiply(BigDecimal.valueOf(hoursToPay)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private Long createBonusPass(Rent rent, LocalDateTime currentDateTime, long timeSubtractionMinutes) {
        Pass bonusPass = new Pass();
        bonusPass.setUser(rent.getUser());
        bonusPass.setModel(rent.getScooter().getModel());
        bonusPass.setTotalMinutes((int) timeSubtractionMinutes);
        bonusPass.setRemainingMinutes(bonusPass.getTotalMinutes());
        bonusPass.setPrice(BigDecimal.ZERO);
        bonusPass.setCreationDate(currentDateTime);
        bonusPass.setExpiredDate(currentDateTime.plusMonths(1));
        return passDao.add(bonusPass);
    }
}
