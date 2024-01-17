package com.example.matbloggen.controller;

import com.example.matbloggen.dtos.LoginRequestDto;
import com.example.matbloggen.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin(origins = "http://localhost:5500/", allowCredentials = "true")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){

        String jwtToken =  userService.generateTokenForUserByEmailAndPassword(loginRequestDto.email, loginRequestDto.password);

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(36000); // Set the cookie expiration time in seconds (adjust as needed)
        cookie.setPath("/"); // Set the cookie path

        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie jwtTokenCookie = new Cookie("jwtToken", "");
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(0);
        jwtTokenCookie.setSecure(false); // Set to true if served over HTTPS
        jwtTokenCookie.setHttpOnly(true);

        response.addCookie(jwtTokenCookie);

        return ResponseEntity.ok("Succesfully logged out");
    }

}