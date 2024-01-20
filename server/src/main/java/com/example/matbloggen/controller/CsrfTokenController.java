package com.example.matbloggen.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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



/*

    @GetMapping("/csrf-token")
    public String getToken(HttpServletRequest request, HttpServletResponse response){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        return "csrfToken.getToken()";
    }

    */
/*
    @GetMapping("/csrf-token")
    public String getToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            setCsrfCookie(response, csrfToken.getToken());
        }

        return csrfToken != null ? csrfToken.getToken() : null;
    }

    private void setCsrfCookie(HttpServletResponse response, String csrfToken) {
        Cookie cookie = new Cookie("X-XSRF-TOKEN", csrfToken);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
    }
    */

}
