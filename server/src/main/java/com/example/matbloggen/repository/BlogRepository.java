package com.example.matbloggen.repository;

import com.example.matbloggen.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {

    List<Blog> findByHeadlineContaining(String searchWord);
    Blog getBlogById(UUID id);
}
