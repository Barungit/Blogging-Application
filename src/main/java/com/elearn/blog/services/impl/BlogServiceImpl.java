package com.elearn.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.elearn.blog.entities.Blog;
import com.elearn.blog.entities.Category;
import com.elearn.blog.entities.User;
import com.elearn.blog.exceptions.ResourceNotFoundException;
import com.elearn.blog.payloads.BlogDto;
import com.elearn.blog.payloads.BlogResponse;
import com.elearn.blog.repositories.BlogRepo;
import com.elearn.blog.repositories.CategoryRepo;
import com.elearn.blog.repositories.UserRepo;
import com.elearn.blog.services.BlogService;
@Service
public class BlogServiceImpl implements BlogService {
	
	@Autowired
	private BlogRepo blogRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	
	
	@Override
	public BlogDto createBlog(BlogDto blogDto, Integer userId, Integer categoryId) {
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		Blog blog = this.modelMapper.map(blogDto, Blog.class);
		//String filename = this.fileService.uploadImage(path, image);
		 //createdBlog.setPicname(filename);
		/*System.out.println(blog.getTitle());
		System.out.println(blog.getContent());
		System.out.println(blog.getUser());
		System.out.println(blog.getCategory());*/
		
		blog.setPicname("Default.png");
		blog.setUploadDate(new Date());
		blog.setUser(user);
		blog.setCategory(category);
		
		Blog newBlog = this.blogRepo.save(blog);
		
		return this.modelMapper.map(newBlog, BlogDto.class);
	}

	@Override
	public BlogDto updateBlog(BlogDto blogDto, Integer Bid) {
		Blog blog = this.blogRepo.findById(Bid).orElseThrow(()-> new ResourceNotFoundException("Blog", "Blog Id", Bid));
		Blog newblog = this.modelMapper.map(blogDto, Blog.class);
		
		Category category = this.categoryRepo.findById(blogDto.getCategory().getCategoryId()).get();
		blog.setTitle(newblog.getTitle());
		blog.setContent(newblog.getContent());
		blog.setPicname(newblog.getPicname());
		blog.setCategory(category);
		Blog updatedBlog = this.blogRepo.save(blog);
		return this.modelMapper.map(updatedBlog, BlogDto.class);
	}

	@Override
	public void deleteBlog(Integer Bid) {
		Blog blog = this.blogRepo.findById(Bid).orElseThrow(()-> new ResourceNotFoundException("Blog", "Blog Id", Bid));
		this.blogRepo.delete(blog);
		
	}

	@Override
	public BlogResponse getallBlogs(Integer pageNumber, Integer pageSize, String sortBy,String sortDir) {
		//Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(sortBy).descending());
		Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		Page<Blog> bloginonepage = this.blogRepo.findAll(p);
		List<Blog> allblog = bloginonepage.getContent();
		List<BlogDto> blogDtos = allblog.stream().map((ab)-> this.modelMapper.map(ab, BlogDto.class)).collect(Collectors.toList());
		
		BlogResponse blogResponse = new BlogResponse();
		blogResponse.setContent(blogDtos);
		blogResponse.setPageNumber(bloginonepage.getNumber());
		blogResponse.setPageSize(bloginonepage.getSize());
		blogResponse.setTotalElements(bloginonepage.getTotalElements());
		blogResponse.setTotalPages(bloginonepage.getTotalPages());
		blogResponse.setLastPage(bloginonepage.isLast());
		
		return blogResponse;
	}
	@Override
	public BlogResponse getallBlogsByCategory(Integer categoryId,
			Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
		Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category ", "Category Id", categoryId));
		Page<Blog> blogs = this.blogRepo.findByCategory(cat,p);
		List<Blog> allblog = blogs.getContent();
		if(blogs.getSize() == 0) {
			throw new ResourceNotFoundException("Any Blog","Category Id",categoryId);
		}
		List<BlogDto> blogDtos = allblog.stream().map((blog) -> this.modelMapper.map(blog, BlogDto.class)).collect(Collectors.toList());
		BlogResponse blogResponse = new BlogResponse();
		blogResponse.setContent(blogDtos);
		blogResponse.setPageNumber(blogs.getNumber());
		blogResponse.setPageSize(blogs.getSize());
		blogResponse.setTotalElements(blogs.getTotalElements());
		blogResponse.setTotalPages(blogs.getTotalPages());
		blogResponse.setLastPage(blogs.isLast());
		
		return blogResponse;
	}

	@Override
	public BlogResponse getallBlogsByUser(Integer uid,Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
		User user = this.userRepo.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User","id",uid));
		Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		Page<Blog> blogs = this.blogRepo.findByUser(user,p) ;
		if(blogs.getSize() == 0) {
			throw new ResourceNotFoundException("No Blog","User Id",uid);
		}
		List<Blog> allblog = blogs.getContent();
		List<BlogDto> blogDto = allblog.stream().map((blog) -> this.modelMapper.map(blog, BlogDto.class)).collect(Collectors.toList());
		BlogResponse blogResponse = new BlogResponse();
		blogResponse.setContent(blogDto);
		blogResponse.setPageNumber(blogs.getNumber());
		blogResponse.setPageSize(blogs.getSize());
		blogResponse.setTotalElements(blogs.getTotalElements());
		blogResponse.setTotalPages(blogs.getTotalPages());
		blogResponse.setLastPage(blogs.isLast());
		
		return blogResponse;
	}

	@Override
	public BlogResponse searchBlogs(String keyword,Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
		Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		Page<Blog> results = this.blogRepo.findByTitleContaining(keyword, p);
		if(results.getTotalPages() == 0) {
			throw new ResourceNotFoundException("Blogs","keywords",keyword);
		}
		List<Blog> allblog = results.getContent();
		List<BlogDto> blogDto = allblog.stream().map((blog) -> this.modelMapper.map(blog, BlogDto.class)).collect(Collectors.toList());
		BlogResponse blogResponse = new BlogResponse();
		blogResponse.setContent(blogDto);
		blogResponse.setPageNumber(results.getNumber());
		blogResponse.setPageSize(results.getSize());
		blogResponse.setTotalElements(results.getTotalElements());
		blogResponse.setTotalPages(results.getTotalPages());
		blogResponse.setLastPage(results.isLast());
		
		return blogResponse;
	}
	@Override
	public BlogDto getBlogbyId(Integer Bid) {
		Blog blog = this.blogRepo.findById(Bid).orElseThrow(() ->new ResourceNotFoundException("Blog", "Blog Id", Bid));
		
		return this.modelMapper.map(blog, BlogDto.class);
	}

}
