package com.ittalent.microservices.users.models.service;



import com.ittalent.microservices.commons.models.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {

    Iterable<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
    String getToken(User user);
    Boolean isValidEmailAddress(String email);

}
