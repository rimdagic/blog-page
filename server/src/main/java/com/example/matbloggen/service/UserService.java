package com.example.matbloggen.service;

import com.example.matbloggen.models.User;
import com.example.matbloggen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean doesUserExist(String email){
        Optional<User> existing = userRepository.findByEmail(email);
        return existing.isPresent();
    }

}