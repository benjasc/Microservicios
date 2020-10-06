package com.ittalent.microservices.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.ittalent.microservices.commons.models.entity.User;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service")
public interface UserFeignClient {


    @GetMapping("/findByEmail")
    public User findByEmail(@RequestParam String email);


}
