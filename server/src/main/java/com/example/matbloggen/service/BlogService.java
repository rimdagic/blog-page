package com.example.matbloggen.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.matbloggen.dtos.PostBlogDto;
import com.example.matbloggen.models.Blog;
import com.example.matbloggen.models.User;
import com.example.matbloggen.repository.BlogRepository;
import com.example.matbloggen.repository.UserRepository;
import com.example.matbloggen.utility.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class BlogService {
    private BlogRepository blogRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public BlogService(BlogRepository blogRepository, UserRepository userRepository, JwtUtil jwtUtil){
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void postBlog(PostBlogDto postBlogDto, String token) {
        try {
            UUID userUUID = UUID.fromString(jwtUtil.extractUserId(token));

            System.out.println("JWT from cookie: " + token);

            // Check if user exists in userRepository
            User user = userRepository.findById(userUUID)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Date date = new Date();

            Blog blog = new Blog();
            blog.setHeadline(postBlogDto.headline);
            blog.setContent(postBlogDto.content);
            blog.setDate(date);
            blog.setUser(user);

            blogRepository.save(blog);

        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Token has expired", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            throw new RuntimeException("JWT verification failed", e);
        }
    }
}
