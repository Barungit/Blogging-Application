package com.elearn.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	
	
}
