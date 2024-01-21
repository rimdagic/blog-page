package com.example.matbloggen.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTVerifyFilter extends OncePerRequestFilter {

    String variableName = "SECRET_KEY";
    String secretKey = System.getenv(variableName);

    private CustomUserDetailsService customUserDetailsService;

    public JWTVerifyFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Check if the current authentication is OAuth2
   //     if (isOAuth2Authentication()) {
   //         filterChain.doFilter(request, response);
   //         return;
   //     }

        Cookie[] cookies = request.getCookies();
        String authCookie = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    authCookie = cookie.getValue();
                }
            }
        }

        if (authCookie.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(authCookie);

            var user = this.customUserDetailsService.loadUserById(decodedJWT.getSubject());
            var auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (JWTVerificationException exception) {
            filterChain.doFilter(request, response);
            throw new IllegalStateException("Failed to authenticate");
        }
    }

    private boolean isOAuth2Authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().startsWith("OAUTH2_USER"));
    }
}
