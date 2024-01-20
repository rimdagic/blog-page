package com.example.matbloggen.controller;

import com.example.matbloggen.dtos.LoginRequestDto;
import com.example.matbloggen.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        cookie.setAttribute("SameSite", "Lax");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        String authority = userService.getAuthorityByEmail(loginRequestDto.email);
        if(authority.equals("ADMIN")){
            System.out.println("admin");
            return ResponseEntity.ok("Login successful admin");
        }
        else if(authority.equals("USER")){
            System.out.println("user");
            return ResponseEntity.ok("Login successful");
        }
        else{
            System.out.println("error");
            return ResponseEntity.ok("not ok");
        }
    }

/*    @GetMapping("/user")
    public ResponseEntity<String> getUserEmail(@RequestParam UUID id){
        String email = userService.getUserEmail(id);
        return ResponseEntity.ok(email);
    }*/

    @GetMapping("/user/email")
    public ResponseEntity<String> getUserEmail(HttpServletRequest request){
        String email = userService.getEmail(request);
        return ResponseEntity.ok().body(email);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie jwtTokenCookie = new Cookie("jwtToken", "");
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(0);
        jwtTokenCookie.setSecure(false); // Set to true if served over HTTPS
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setAttribute("SameSite", "Lax");

        response.addCookie(jwtTokenCookie);


        Cookie cookie = new Cookie("XSRF-TOKEN", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Succesfully logged out");
    }

    @GetMapping("/user/auth")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request){
        boolean isAuthenticated = userService.checkAuth(request);
        return ResponseEntity.ok(isAuthenticated);
    }

}