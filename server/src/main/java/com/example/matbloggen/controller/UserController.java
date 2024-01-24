package com.example.matbloggen.controller;

import com.example.matbloggen.dtos.LoginRequestDto;
import com.example.matbloggen.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin(origins = "http://localhost:5500/", allowCredentials = "true")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request) {
        if (loginRequestDto.email.isBlank() || loginRequestDto.password.isBlank() ||
                loginRequestDto.email.length() > 320 || loginRequestDto.password.length() > 99) {
            throw new RuntimeException("Invalid input");
        } else {

            String jwtToken = userService.generateTokenForUserByEmailAndPassword(loginRequestDto.email, loginRequestDto.password);

            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setMaxAge(36000);
            cookie.setPath("/");
            cookie.setAttribute("SameSite", "Lax");
            cookie.setSecure(false);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

            Cookie jsessionCookie = new Cookie("JSESSIONID", request.getSession().getId());
            jsessionCookie.setMaxAge(-1);
            jsessionCookie.setPath("/");

            jsessionCookie.setAttribute("SameSite", "Lax");

            response.addCookie(jsessionCookie);

            String authority = userService.getAuthorityByEmail(loginRequestDto.email);
            if (authority.equals("ADMIN")) {
                System.out.println("admin");
                return ResponseEntity.ok("Login successful admin");
            } else if (authority.equals("USER")) {
                System.out.println("user");
                return ResponseEntity.ok("Login successful");
            } else {
                System.out.println("error");
                return ResponseEntity.ok("not ok");
            }
        }
    }

    @GetMapping("/user/google")
    private void google(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient client, HttpServletResponse response) throws GeneralSecurityException, IOException {

        String accessToken = client.getAccessToken().getTokenValue();
        String userEmail = userService.getUserEmailUsingAccessToken(accessToken);

        String jwtToken = userService.createUser(userEmail);

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(36000);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        System.out.println("google jwt token: " + jwtToken);
        response.addCookie(cookie);

        response.sendRedirect("http://localhost:5500/home.html");
    }

    @GetMapping("/user/email")
    public ResponseEntity<String> getUserEmail(HttpServletRequest request) {
        String email = userService.getEmail(request);
        return ResponseEntity.ok().body(email);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie jwtTokenCookie = new Cookie("jwtToken", null);
        jwtTokenCookie.setPath("/");
        jwtTokenCookie.setMaxAge(0);
        jwtTokenCookie.setSecure(false); // Set to true if served over HTTPS
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setAttribute("SameSite", "Lax");
        response.addCookie(jwtTokenCookie);

        Cookie jsessionid = new Cookie("JSESSIONID", null);
        jsessionid.setPath("/");
        jsessionid.setMaxAge(0);
        response.addCookie(jsessionid);

        Cookie cookie = new Cookie("XSRF-TOKEN", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Succesfully logged out");
    }

    @GetMapping("/user/auth")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        boolean isAuthenticated = userService.checkAuth(request);
        return ResponseEntity.ok(isAuthenticated);
    }
}