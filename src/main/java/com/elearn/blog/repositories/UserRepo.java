package com.elearn.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	
	Optional<User> email(String email);
}
