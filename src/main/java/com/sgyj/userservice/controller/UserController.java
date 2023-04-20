package com.sgyj.userservice.controller;

import com.sgyj.userservice.dto.UserDto;
import com.sgyj.userservice.service.UserService;
import com.sgyj.userservice.vo.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health_check")
    public String status() {
        return "user service working!";
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RequestUser requestUser) {
        return new ResponseEntity<>(userService.createUser(new UserDto(requestUser)), HttpStatus.CREATED );
    }

}
