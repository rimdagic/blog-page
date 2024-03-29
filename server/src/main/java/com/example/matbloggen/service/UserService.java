package com.example.matbloggen.service;

import com.example.matbloggen.exceptions.EmailNotFoundException;
import com.example.matbloggen.exceptions.WrongPasswordException;
import com.example.matbloggen.models.User;
import com.example.matbloggen.repository.UserRepository;
import com.example.matbloggen.utility.JwtUtil;
import com.example.matbloggen.utility.PasswordEncoderUtil;
import com.example.matbloggen.utility.PasswordGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoderUtil passwordEncoderUtil;
    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderUtil passwordEncoderUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
        this.jwtUtil = jwtUtil;
    }

    public boolean doesUserExist(String email) {
        Optional<User> existing = userRepository.findByEmail(email);
        return existing.isPresent();
    }

    public String generateTokenForUserByEmailAndPassword(String email, String password) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoderUtil.verifyPassword(password, user.getPassword())) {
                    return jwtUtil.createToken(String.valueOf(user.getId()), user.getEmail());
                } else {
                    throw new WrongPasswordException("wrong password, cannot generate token");
                }
            } else {
                throw new EmailNotFoundException("Email not found");
            }
        } catch (EmailNotFoundException e) {
            return e.getMessage();
        } catch (WrongPasswordException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "error when getting email";
        }
    }

    public String getAuthorityByEmail(String email) {
        Optional<User> OptionalUser = userRepository.findByEmail(email);
        if (OptionalUser.isPresent()) {
            User user = OptionalUser.get();
            return user.getAuthority();
        }
        return "no authority";
    }

    public boolean checkAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    try {
                        jwtUtil.extractUserId(cookie.getValue());
                        return true;
                    } catch (Exception e) {
                        System.out.println("Could not validate jwt token");
                    }
                }
            }
        }
        return false;
    }

    public String getEmail(HttpServletRequest request) {
        String jwt = getJwtFromCookie(request);
        String userId = jwtUtil.extractUserId(jwt);
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
        if (optionalUser.isPresent()) {
            return optionalUser.get().getEmail();
        } else return null;
    }

    public String getJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    try {
                        return cookie.getValue();
                    } catch (Exception e) {
                        System.out.println("Could not validate jwt token");
                    }
                }
            }
        }
        return null;
    }


    public String getUserEmailUsingAccessToken(String accessToken) {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        ResponseEntity<Map> responseEntity = new RestTemplate().getForEntity(userInfoEndpoint, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> userInfo = responseEntity.getBody();
            return (String) userInfo.get("email");
        } else {
            return null;
        }
    }

    public String createUser(String userEmail) {
        if (!userRepository.findByEmail(userEmail).isPresent()) {
            User user = new User();
            user.setEmail(userEmail);
            user.setPassword(passwordEncoderUtil.encodePassword(PasswordGenerator.generateRandomPassword(100)));
            user.setAuthority("USER");
            userRepository.save(user);
        }

        return jwtUtil.createToken(userRepository.findByEmail(userEmail).get().getId().toString(), userEmail);
    }

}




