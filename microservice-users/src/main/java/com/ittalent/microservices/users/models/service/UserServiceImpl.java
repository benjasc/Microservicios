package com.ittalent.microservices.users.models.service;

import com.google.gson.Gson;
import com.ittalent.microservices.commons.models.entity.User;

import com.ittalent.microservices.users.dto.TokenDto;
import com.ittalent.microservices.users.models.repository.IUserRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional findById(Long id) {
        return iUserRepository.findById(id);
    }


    @Override
    @Transactional
    public User save(User user) {
        return iUserRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        iUserRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public String getToken(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String plainCreds = "backendApp:12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        log.debug("token Base64:" + base64Creds);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.set("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("username", "benjamin.salazar17gmail.com");
        body.add("password", "123");
        body.add("grant_type", "password");

        HttpEntity<?> request = new HttpEntity<Object>(body, headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange("http://localhost:8090/api/security/oauth/token", HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.error("Service error :" + e.getStackTrace());
            return null;
        }
        log.debug("service consumed successfully!");
        response.getHeaders().getLocation();
        String responseBody = response.getBody();
        TokenDto data = new Gson().fromJson(responseBody, TokenDto.class);
        return data.getAccess_token();
    }

    @Override
    public Boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
