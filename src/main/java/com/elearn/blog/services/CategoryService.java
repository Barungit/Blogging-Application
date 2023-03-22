package com.elearn.blog.services;

import java.util.List;

import com.elearn.blog.payloads.CategoryDto;

public interface CategoryService {
	
	CategoryDto createCategory(CategoryDto categoryDto);
	
	//update
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	
	//delete
	 void deleteCategory(Integer categoryId);
	
	//get all
	List<CategoryDto> getCategories();
	
	//get single
	CategoryDto getCategory(Integer categoryId);
}
