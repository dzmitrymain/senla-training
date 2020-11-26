package com.senla.training.yeutukhovich.scooterrental.service.pass;

import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Role;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.PassDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.model.ModelService;
import com.senla.training.yeutukhovich.scooterrental.service.user.UserService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class PassServiceImpl implements PassService {

    private static final String ENTITY_NAME = "Pass";

    private final PassDao passDao;
    private final PassDtoMapper passDtoMapper;
    private final ModelService modelService;
    private final UserService userService;

    @Value("${PassServiceImpl.passPriceCoefficient:0.5}")
    private BigDecimal passPriceCoefficient;

    @Autowired
    public PassServiceImpl(PassDao passDao,
                           PassDtoMapper passDtoMapper,
                           ModelService modelService,
                           UserService userService) {
        this.passDao = passDao;
        this.passDtoMapper = passDtoMapper;
        this.modelService = modelService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public List<PassDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return passDao.findAll().stream()
                .map(passDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PassDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return passDtoMapper.map(findPassById(id));
    }

    @Override
    @Transactional
    public PassDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Pass passToDelete = findPassById(id);
        passDao.delete(passToDelete);
        return passDtoMapper.map(passToDelete);
    }

    @Override
    @Transactional
    public PassDto updateById(Long id, @Valid PassDto passDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findPassById(id);
        modelService.findById(passDto.getModelDto().getId());
        userService.findById(passDto.getUserDto().getId());
        if (!id.equals(passDto.getId())) {
            passDto.setId(id);
        }
        return passDtoMapper.map(passDao.update(passDtoMapper.map(passDto)));
    }

    @Override
    @Transactional
    public PassDto create(@Valid PassDto passDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        passDto.setModelDto(modelService.findById(passDto.getModelDto().getId()));
        passDto.setUserDto(userService.findById(passDto.getUserDto().getId()));
        RateDto rateDto = modelService.findCurrentModelRate(passDto.getModelDto().getId());
        Pass pass = passDtoMapper.map(passDto);
        pass.setId(null);
        final LocalDateTime currentDateTime = LocalDateTime.now();
        pass.setCreationDate(currentDateTime);
        pass.setTotalMinutes(passDto.getTotalMinutes());
        pass.setExpiredDate(currentDateTime.plusMonths(1));
        if (passDto.getUserDto().getRole().equals(Role.USER.name())) {
            pass.setPrice(calculatePassPrice(pass.getTotalMinutes(), rateDto.getPerHour()));
        } else if (passDto.getUserDto().getRole().equals(Role.ADMIN.name())) {
            if (passDto.getExpiredDate() != null) {
                pass.setExpiredDate(passDto.getExpiredDate());
            }
            pass.setPrice(BigDecimal.ZERO);
        }
        pass.setRemainingMinutes(pass.getTotalMinutes());
        passDao.add(pass);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, pass.getId());
        return passDtoMapper.map(pass);
    }

    @Override
    @Transactional
    public List<PassDto> findAllActivePasses() {
        log.info(LoggerConstant.PASSES_ACTIVE.getMessage());
        return passDao.findAllActivePasses().stream()
                .map(passDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Pass findPassById(Long passId) {
        return passDao.findById(passId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }

    private BigDecimal calculatePassPrice(long passMinutes, BigDecimal perHour) {
        BigDecimal passPricePerMinute = perHour.multiply(passPriceCoefficient).divide(new BigDecimal(60), 2,
                RoundingMode.HALF_EVEN);
        return passPricePerMinute.multiply(new BigDecimal(passMinutes));
    }
}
