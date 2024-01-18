package com.example.matbloggen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        http
                /*.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))*/
                .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()))
/*                .csrf((csrf) -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                )*/
                /*.csrf(csrf -> csrf.disable())*/
                /*.csrf(withDefaults())*/
                .addFilterAfter(new JWTVerifyFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/logout").permitAll()
                        .requestMatchers("/all-posts").permitAll()
                        .requestMatchers("/user/auth").permitAll()
                        .requestMatchers("/search-posts").permitAll()
                        .requestMatchers("/post").permitAll()

                        .requestMatchers("/blog-post").hasAuthority("USER")
                        .requestMatchers("/blog-post").hasAuthority("ADMIN")
                        .requestMatchers("/delete-posts").hasAuthority("ADMIN")
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

}
