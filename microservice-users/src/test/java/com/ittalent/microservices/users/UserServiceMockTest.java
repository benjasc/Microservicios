package com.ittalent.microservices.users;

import com.ittalent.microservices.commons.models.entity.Phone;
import com.ittalent.microservices.commons.models.entity.User;
import com.ittalent.microservices.users.models.repository.IUserRepository;
import com.ittalent.microservices.users.models.service.IUserService;
import com.ittalent.microservices.users.models.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceMockTest {

    private Logger log = LoggerFactory.getLogger(UserServiceMockTest.class);

    @Mock
    private IUserRepository iUserRepository;

    @Autowired
    private IUserService iUserService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        iUserService = new UserServiceImpl(iUserRepository);
        User user = new User();
        Phone phone = new Phone();
        List<Phone> phones = new ArrayList<>();

        phone.setCityCode("123");
        phone.setCountryCode("1234");
        phone.setNumber("966791415");
        phones.add(phone);

        user.setId(100L);
        user.setName("TestUserABC");
        user.setEmail("testUser@gmail.com");
        user.setPassword("qwerty123");
        user.setLastLogin(new Date());
        user.setUpdatedAt(new Date());
        user.setCreatedAt(new Date());
        user.setIsActive("Y");
        user.setPhones(phones);

        Mockito.when(iUserRepository.findById(100L)).thenReturn(Optional.of(user));

    }

    @Test
    public void findUser(){
        Optional<User> found = iUserService.findById(100L);
        log.info("**** USER ****");
        log.info("Name: "+found.get().getName());
        log.info("Email: "+found.get().getEmail());
        log.info("isActive: "+found.get().getIsActive());
        log.info("**************");
        Assertions.assertThat(found.get().getName()).isEqualTo("TestUserABC");
    }

    @Test
    public void emailValidation(){
        Optional<User> found = iUserService.findById(100L);
        boolean isValid = iUserService.isValidEmailAddress(found.get().getEmail());
        if(isValid){
            log.info("Valid Email");
        }
        Assertions.assertThat(isValid).isTrue();
    }

    //It is necessary to run all microservices
    @Test
    public void getToken(){
        String token = iUserService.getToken(new User());
        if(token!=null){
            log.info("*** token received ***");
            log.info("TOKEN :"+token);
        }
        Assertions.assertThat(token).isNotNull();
    }

}
