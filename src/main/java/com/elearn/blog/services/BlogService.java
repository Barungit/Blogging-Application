package com.elearn.blog.services;
import com.elearn.blog.payloads.BlogDto;
import com.elearn.blog.payloads.BlogResponse;

public interface BlogService {
	
	//create
	BlogDto createBlog(BlogDto blogDto, Integer userId, Integer categoryId);
	//update
	BlogDto updateBlog(BlogDto blogDto,Integer Bid);
	//approve
		BlogDto approveBlog(Integer Bid);
	//delte
	void deleteBlog(Integer Bid);
	//get all
	BlogResponse getallBlogs(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
	//get single blog
	BlogDto getBlogbyId(Integer Bid);
	//get all by category
	BlogResponse getallBlogsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
	//get all by user
	BlogResponse getallBlogsByUser(Integer uid,Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
	//search post
	BlogResponse searchBlogs(String keyword,Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
}
