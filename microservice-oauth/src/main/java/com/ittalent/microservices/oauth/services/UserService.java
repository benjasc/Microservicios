package com.ittalent.microservices.oauth.services;

import com.ittalent.microservices.commons.models.entity.User;
import com.ittalent.microservices.oauth.clients.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Service
public class UserService implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient client;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = client.findByEmail(email);

        if (user == null) {
            log.error("Error, no existe el usuario en el sistema");
            throw new UsernameNotFoundException("Error, no existe el usuario con email -> '" + email + "' en el sistema");
        }

        log.info("Usuario en el sistema: " + email);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, new HashSet<GrantedAuthority>());
    }
}
