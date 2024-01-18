package com.example.matbloggen.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Map<String, String> csrfTokenMap = new HashMap<>();
        csrfTokenMap.put("csrfToken", csrfToken.getToken());

        System.out.println(csrfTokenMap);
        return csrfTokenMap;
    }
}
