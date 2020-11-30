package com.senla.training.yeutukhovich.scooterrental.service.pass;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.pass.PassDao;
import com.senla.training.yeutukhovich.scooterrental.dao.user.UserDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.role.Role;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.PassDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PassServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Pass PASS = Mockito.mock(Pass.class);
    private static final PassDto PASS_DTO = Mockito.mock(PassDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);
    private static final User USER = Mockito.mock(User.class);
    private static final UserDto USER_DTO = Mockito.mock(UserDto.class);
    private static final Rate RATE = Mockito.mock(Rate.class);

    @Autowired
    private PassService passService;

    @Autowired
    private PassDao passDao;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PassDtoMapper passDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(PASS.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PASS_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(PASS_DTO.getModelDto()).thenReturn(MODEL_DTO);
        Mockito.when(PASS_DTO.getUserDto()).thenReturn(USER_DTO);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(USER_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(USER_DTO.getRole()).thenReturn(Role.ADMIN.name());
    }

    @BeforeEach
    void setUp() {
        Mockito.when(passDtoMapper.map(PASS)).thenReturn(PASS_DTO);
        Mockito.when(passDtoMapper.map(PASS_DTO)).thenReturn(PASS);
        Mockito.clearInvocations(passDao, modelDao, userDao);
    }

    @Test
    void PassServiceImpl_findAllActivePasses() {
        passService.findAllActivePasses();

        Mockito.verify(passDao, Mockito.times(1)).findAllActivePasses();
    }

    @Test
    void PassServiceImpl_findAll() {
        passService.findAll();

        Mockito.verify(passDao, Mockito.times(1)).findAll();
    }

    @Test
    void PassServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PASS));

        PassDto passDto = passService.findById(ENTITY_ID);

        Assertions.assertNotNull(passDto);
    }

    @Test
    void PassServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.findById(ENTITY_ID));
    }

    @Test
    void PassServiceImpl_deleteById() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PASS));

        PassDto passDto = passService.deleteById(ENTITY_ID);

        Mockito.verify(passDao, Mockito.times(1)).delete(PASS);
        Assertions.assertNotNull(passDto);
    }

    @Test
    void PassServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.deleteById(ENTITY_ID));
    }

    @Test
    void PassServiceImpl_updateById() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PASS));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(passDao.update(PASS)).thenReturn(PASS);

        PassDto passDto = passService.updateById(ENTITY_ID, PASS_DTO);

        Mockito.verify(passDao, Mockito.times(1)).update(PASS);
        Assertions.assertNotNull(passDto);
    }

    @Test
    void PassServiceImpl_updateById_shouldThrowBusinessExceptionIfPassNotExist() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.updateById(ENTITY_ID, PASS_DTO));
    }

    @Test
    void PassServiceImpl_updateById_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PASS));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.updateById(ENTITY_ID, PASS_DTO));
    }

    @Test
    void PassServiceImpl_updateById_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(passDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PASS));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.updateById(ENTITY_ID, PASS_DTO));
    }

    @Test
    void PassServiceImpl_create() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));

        PassDto passDto = passService.create(PASS_DTO);

        Mockito.verify(passDao, Mockito.times(1)).add(PASS);
        Assertions.assertNotNull(passDto);
    }

    @Test
    void PassServiceImpl_create_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));

        Assertions.assertThrows(BusinessException.class, () -> passService.create(PASS_DTO));
    }

    @Test
    void PassServiceImpl_create_shouldThrowBusinessExceptionIfUserNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.of(MODEL));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.create(PASS_DTO));
    }

    @Test
    void PassServiceImpl_create_shouldThrowBusinessExceptionIfRateNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.of(MODEL));
        Mockito.when(userDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(USER));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> passService.create(PASS_DTO));
    }
}
