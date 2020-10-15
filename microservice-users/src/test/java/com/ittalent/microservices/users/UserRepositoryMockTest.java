package com.ittalent.microservices.users;

import com.ittalent.microservices.commons.models.entity.Phone;
import com.ittalent.microservices.commons.models.entity.User;
import com.ittalent.microservices.users.models.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@DataJpaTest
public class UserRepositoryMockTest {

    private Logger log = LoggerFactory.getLogger(UserRepositoryMockTest.class);

    @Autowired
    private IUserRepository iUserRepository;

    //import.sql only has one user
    @Test
    public void findAllUsers(){
        List<User> users = (List<User>) iUserRepository.findAll();
        users.stream().forEach((user)->{
            log.info("**** USER ****");
            log.info("Name: "+user.getName());
            log.info("Email: "+user.getEmail());
            log.info("isActive: "+user.getIsActive());
            log.info("**************");
        });
        Assertions.assertEquals(users.size(),1);
    }

    @Test
    public void findUserInserted(){
        User user = new User();
        Phone phone = new Phone();
        List<Phone> phones = new ArrayList<>();

        phone.setCityCode("123");
        phone.setCountryCode("1234");
        phone.setNumber("966791415");
        phones.add(phone);

        user.setName("TestUser");
        user.setEmail("testUser@gmail.com");
        user.setPassword("qwerty123");
        user.setLastLogin(new Date());
        user.setUpdatedAt(new Date());
        user.setCreatedAt(new Date());
        user.setIsActive("Y");
        user.setPhones(phones);

        iUserRepository.save(user);
        User userDB = iUserRepository.findByEmail("testUser@gmail.com").get();

        log.info("**** USER ****");
        log.info("Name: "+userDB.getName());
        log.info("Email: "+userDB.getEmail());
        log.info("isActive: "+userDB.getIsActive());
        log.info("**************");

        Assertions.assertEquals("testUser@gmail.com", userDB.getEmail());

    }

    @Test
    public void deleteUser(){
        Optional<User> user = iUserRepository.findById(1L);
        if(user.isPresent()){
            iUserRepository.deleteById(1L);
            log.info("User Deleted");
        }
        Assertions.assertTrue(!iUserRepository.findById(1L).isPresent());
    }



}
