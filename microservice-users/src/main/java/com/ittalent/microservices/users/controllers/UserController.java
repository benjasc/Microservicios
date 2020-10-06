package com.ittalent.microservices.users.controllers;

import com.ittalent.microservices.commons.models.entity.User;
import com.ittalent.microservices.users.models.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    @GetMapping
    public ResponseEntity<?> getUserList() {
        return ResponseEntity.ok().body(iUserService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, Model model) {
        Optional<User> u = iUserService.findById(id);
        if (!u.isPresent()) {
            model.addAttribute("Error", "user not found");
            log.debug("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
        log.debug("user was found!");
        return ResponseEntity.ok(u.get());
    }

    @PostMapping
    public Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> u = iUserService.findByEmail(user.getEmail());
        if (u.isPresent()) {
            response.put("Error", "user already exist");
            log.debug("user already exist");
            return response;
        }

        if(!iUserService.isValidEmailAddress(user.getEmail())){
            response.put("Error", "Email does not have a valid format");
            log.debug("email does not have a valid format");
            return response;

        }

        User userDb = user;

        userDb.setUpdatedAt(new Date());
        userDb.setLastLogin(new Date());
        userDb.setIsActive("Y");
        userDb.setToken(iUserService.getToken(user));
        User userSaved = iUserService.save(userDb);

        log.debug("user was persisted successfully");

        response.put("id", userSaved.getId());
        response.put("created", userSaved.getCreatedAt());
        response.put("modified", userSaved.getUpdatedAt());
        response.put("last_login", userSaved.getLastLogin());
        response.put("token", userSaved.getToken());
        response.put("isActive", userSaved.getIsActive());

        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@RequestBody User user, @PathVariable Long id) {
        Optional<User> u = iUserService.findById(id);
        if (!u.isPresent()) {
            log.debug("user not found");
            return ResponseEntity.notFound().build();
        }
        User userDb = u.get();
        userDb.setName(user.getName());
        userDb.setPassword(user.getPassword());
        userDb.setEmail(user.getEmail());
        Date date = new Date();
        userDb.setUpdatedAt(date);
        userDb.setPhones(user.getPhones());
        log.debug("updating user data...");
        return ResponseEntity.status(HttpStatus.CREATED).body(iUserService.save(userDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        iUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<?> findByEmail(@RequestParam(name = "email") String email) {
        Optional<User> u = iUserService.findByEmail(email);
        if (!u.isPresent()) {
            log.debug("user not found");
            return ResponseEntity.notFound().build();
        }
        log.debug("user was found");
        return ResponseEntity.ok(u.get());
    }

}
