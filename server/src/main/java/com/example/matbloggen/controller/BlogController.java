package com.example.matbloggen.controller;

import com.example.matbloggen.dtos.PostBlogDto;
import com.example.matbloggen.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class BlogController {

    private BlogService blogService;

    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @PostMapping("/blog-post")
    public ResponseEntity<String> post(@RequestBody PostBlogDto postBlogDto){
        blogService.postBlog(postBlogDto);
        return ResponseEntity.ok("Post success");
    }
}
