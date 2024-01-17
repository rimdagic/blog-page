package com.example.matbloggen.service;

import com.example.matbloggen.exceptions.EmailNotFoundException;
import com.example.matbloggen.exceptions.WrongPasswordException;
import com.example.matbloggen.models.User;
import com.example.matbloggen.repository.UserRepository;
import com.example.matbloggen.utility.JwtUtil;
import com.example.matbloggen.utility.PasswordEncoderUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //generates a token based on email and password, since email is more unique than a name
    public String generateTokenForUserByEmailAndPassword(String email, String password) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                //will compare the raw password with the hashed one along with the salt, if they match, its ok
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

/*    public String getUserEmail(UUID id) {
        User user = userRepository.findEmailById(id);
        return user.getEmail();
    }*/
}