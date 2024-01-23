package com.example.matbloggen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        http
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                               .ignoringRequestMatchers("/user/login")
                        .ignoringRequestMatchers("/user/logout"));

        http
                .addFilterAfter(new JWTVerifyFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/delete-posts").hasAuthority("ADMIN")
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/logout").permitAll()
                        .requestMatchers("/all-posts").permitAll()
                        .requestMatchers("/user/auth").permitAll()
                        .requestMatchers("/search-posts").permitAll()
                        .requestMatchers("/post").permitAll()

                        .requestMatchers("/user/google").authenticated()

                        .requestMatchers("/blog-post").hasAnyAuthority("USER", "OAUTH2_USER")


                        .requestMatchers("/user/email").authenticated()
                        .requestMatchers("/csrf-token").authenticated()
                )

                .oauth2Login(Customizer.withDefaults())

                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
