package com.example.matbloggen.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
@RestController
public class CsrfTokenController {


    @GetMapping("/csrf-token")
    public void getCsrfToken(CsrfToken csrfToken) {
        System.out.println(csrfToken.getToken());
        //return csrfToken.getToken();
    }
}
