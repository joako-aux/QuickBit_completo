package com.QuickBite.UserServiceQuickBite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users-")
public class UserController {

    @GetMapping("/test")
    public String test() {
        return "User Service OK";
    }

    @GetMapping("/profile")
    public String me(

            @RequestHeader("X-User-Email")
            String email,

            @RequestHeader("X-User-Role")
            String role
    ) {

        return email + " | " + role;
    }
}