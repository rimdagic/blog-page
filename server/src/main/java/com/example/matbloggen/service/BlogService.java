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
import java.util.List;
import java.util.UUID;

@Service
public class BlogService {
    private BlogRepository blogRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public BlogService(BlogRepository blogRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Blog> getAll() {
        List allPosts = blogRepository.findAll();
        return allPosts;
    }

    public void postBlog(PostBlogDto postBlogDto, String token) {

        if (postBlogDto.headline.isBlank() || postBlogDto.content.isBlank() ||
                postBlogDto.headline.length() > 127 || postBlogDto.content.length() > 4095) {
            throw new RuntimeException("Invalid input");
        } else {
            try {
                UUID userUUID = UUID.fromString(jwtUtil.extractUserId(token));

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

    public List<Blog> getSpecific(String searchWord) {
        List<Blog> searchResult = blogRepository.findByHeadlineContaining(searchWord);
        return searchResult;
    }

    public void deleteAllBlogPosts() {
        blogRepository.deleteAll();
    }

    public Blog getBlogById(UUID id) {
        System.out.println("uuid " + id.toString());
        return blogRepository.getBlogById(id);
    }
}