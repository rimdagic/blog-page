package com.example.matbloggen.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
@RestController
public class CsrfTokenController {


    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Map<String, String> csrfTokenMap = new HashMap<>();
        csrfTokenMap.put("csrfToken", csrfToken.getToken());

        System.out.println(csrfTokenMap);

        Cookie cookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        return csrfTokenMap;
    }
}
