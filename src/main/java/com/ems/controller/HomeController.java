package com.ems.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "EMS Backend is running!";
    }

    @GetMapping("/api/admins")
    public String testAdmins() {
        return "Admins endpoint working!";
    }
}
