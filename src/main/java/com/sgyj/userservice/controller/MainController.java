package com.sgyj.userservice.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/health")
    @Timed(value = "account.status", longTask = true)
    public String health() {
        return "health";
    }

}
