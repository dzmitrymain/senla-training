package com.senla.training.yeutukhovich.scooterrental.service.pass;

import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.dto.PassDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.PassDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PassServiceImpl implements PassService {

    private static final String ENTITY_NAME = "Pass";

    private final PassDao passDao;
    private final PassDtoMapper passDtoMapper;

    @Autowired
    public PassServiceImpl(PassDao passDao, PassDtoMapper passDtoMapper) {
        this.passDao = passDao;
        this.passDtoMapper = passDtoMapper;
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
    public PassDto updateById(Long id, PassDto passDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findPassById(id);
        if (!id.equals(passDto.getId())) {
            passDto.setId(id);
        }
        return passDtoMapper.map(passDao.update(passDtoMapper.map(passDto)));
    }

    @Override
    @Transactional
    public PassDto create(PassDto passDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Pass pass = passDtoMapper.map(passDto);
        pass.setId(null);
        passDao.add(pass);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), pass.getId());
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
        return passDao.findById(passId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
