package com.ittalent.microservices.users.models.repository;


import com.ittalent.microservices.commons.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u where u.email=?1")
    public Optional<User> findByEmail(String email);

}
