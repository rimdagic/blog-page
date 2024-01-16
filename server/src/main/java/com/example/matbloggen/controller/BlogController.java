package com.example.matbloggen.controller;

import com.example.matbloggen.dtos.PostBlogDto;
import com.example.matbloggen.models.Blog;
import com.example.matbloggen.service.BlogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BlogController {

    private BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @CrossOrigin(origins = "http://localhost:5500/", allowCredentials = "true")
    @PostMapping("/blog-post")
    public ResponseEntity<String> post(@RequestBody PostBlogDto postBlogDto, HttpServletRequest request) {

        String jwt = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    blogService.postBlog(postBlogDto, jwt);

                    return ResponseEntity.ok("Post success");
                }
            }
        }

        return ResponseEntity.status(401).body("Could not create new post due to: Unable to authenticate user");
    }

    @GetMapping("/all-posts")
    public ResponseEntity<List> getPosts() {
        return ResponseEntity.ok(blogService.getAll());
    }


    @GetMapping("/search-posts")
    public ResponseEntity<List> findPosts(@RequestParam String searchWord) {
        return ResponseEntity.ok(blogService.getSpecific(searchWord));
    }


    @CrossOrigin(origins = "http://localhost:5500/", allowCredentials = "true")
    @DeleteMapping("/delete-posts")
    public ResponseEntity<String> deleteAllPosts() {
        blogService.deleteAllBlogPosts();
        return ResponseEntity.ok("All blog posts in 'Matbloggen' has been deleted.");
    }

}
