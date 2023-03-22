package com.elearn.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
