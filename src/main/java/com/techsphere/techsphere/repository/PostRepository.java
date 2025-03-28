package com.techsphere.techsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techsphere.techsphere.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{}