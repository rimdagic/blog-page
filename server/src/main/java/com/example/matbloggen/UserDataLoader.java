package com.example.matbloggen;

import com.example.matbloggen.models.User;
import com.example.matbloggen.repository.UserRepository;
import com.example.matbloggen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    public UserDataLoader(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        createUser("one@one.com", "pass123", "USER");
        createUser("two@one.com", "pass123", "USER");
        createUser("three@one.com", "pass123", "USER");
        createUser("four@one.com", "pass123", "ADMIN");
        createUser("five@one.com", "pass123", "USER");
    }

    private void createUser(String email, String password, String authority){
        if(!userService.doesUserExist(email)) {
            String userEmail = email;
            String userPassword = password;
            String userAuthority = authority;
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);
            user.setEmail(userEmail);
            user.setPassword(userPassword);
            user.setAuthority(userAuthority);

            userRepository.save(user);
            System.out.println("Användaren har lagts till i databasen.");
        }
    }
}