package com.elearn.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
import com.elearn.blog.payloads.CommentDto;
import com.elearn.blog.payloads.UserDto;
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
		System.out.println("Create blog sefvice impl");
		
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
		Category category = null;
		if(blogDto.getCategory().getCategoryId()==null) {
			 category = this.categoryRepo.findById(blog.getCategory().getCategoryId()).get();
		}else {
			 category = this.categoryRepo.findById(blogDto.getCategory().getCategoryId()).get();
		}
		
		blog.setTitle(newblog.getTitle());
		blog.setContent(newblog.getContent());
		blog.setPicname(newblog.getPicname());
		blog.setCategory(category);
		Blog updatedBlog = this.blogRepo.save(blog);
		return this.modelMapper.map(updatedBlog, BlogDto.class);
	}
	
	@Override
	public BlogDto approveBlog(Integer Bid) {
		Blog blog = this.blogRepo.findById(Bid).orElseThrow(()-> new ResourceNotFoundException("Blog", "Blog Id", Bid));
		blog.setVisible(true);
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
		/*List<BlogDto> blogDtos = allblog.stream().map((ab)-> this.modelMapper.map(ab, BlogDto.class)).collect(Collectors.toList());*/
		
		List<BlogDto> blogDtos = getBlogDtos(allblog);

		
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
		List<BlogDto> blogDtos = getBlogDtos(allblog);
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
		List<BlogDto> blogDtos = getBlogDtos(allblog);
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
	public BlogResponse searchBlogs(String keyword,Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
		Pageable p = PageRequest.of(pageNumber, pageSize,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		Page<Blog> results = this.blogRepo.findByTitleContaining(keyword, p);
		if(results.getTotalPages() == 0) {
			throw new ResourceNotFoundException("Blogs","keywords",keyword);
		}
		List<Blog> allblog = results.getContent();
		List<BlogDto> blogDtos = getBlogDtos(allblog);
		BlogResponse blogResponse = new BlogResponse();
		blogResponse.setContent(blogDtos);
		blogResponse.setPageNumber(results.getNumber());
		blogResponse.setPageSize(results.getSize());
		blogResponse.setTotalElements(results.getTotalElements());
		blogResponse.setTotalPages(results.getTotalPages());
		blogResponse.setLastPage(results.isLast());
		
		return blogResponse;
	}
	
	public List<BlogDto> getBlogDtos(List<Blog> allblog){
		return allblog.stream()
	            .map(blog -> {
	                // Map the blog to the DTO
	                BlogDto blogDto = this.modelMapper.map(blog, BlogDto.class);

	                // Fetch the user information and set it in the UserDto object
	                UserDto userDto = this.modelMapper.map(blog.getUser(), UserDto.class);
	                blogDto.setUser(userDto);

	                // Fetch the comments and set them in the BlogDto object
	                Set<CommentDto> commentDtos = blog.getComments().stream()
	                        .map(comment -> {
	                            CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
	                            commentDto.setUserId(comment.getUser().getUid());
	                            commentDto.setCommentAuthor(comment.getUser().getName());
	                            return commentDto;
	                        })
	                        .collect(Collectors.toSet());
	                blogDto.setComments(commentDtos);

	                return blogDto;
	            })
	            .collect(Collectors.toList());
	}
	
	@Override
	public BlogDto getBlogbyId(Integer Bid) {
		Blog oldblog = this.blogRepo.findById(Bid).orElseThrow(() ->new ResourceNotFoundException("Blog", "Blog Id", Bid));
		if(oldblog.getView()==null) {
			oldblog.setView((long) 1);
		}else {
			oldblog.setView(oldblog.getView()+1);
		}
		Blog blog = this.blogRepo.save(oldblog);
		System.out.println(blog.getView());
		BlogDto blogDto =this.modelMapper.map(blog, BlogDto.class);
		 // Fetch the user information and set it in the UserDto object
	    User user = userRepo.findById(blog.getUser().getUid())
	            .orElseThrow(() -> new ResourceNotFoundException("User", "User ID", blog.getUser().getUid()));
	    UserDto userDto = modelMapper.map(user, UserDto.class);
	    blogDto.setUser(userDto);
	    System.out.println(blogDto.getView()/2);
	    // Fetch the comments and set them in the BlogDto object
	    Set<CommentDto> commentDtos = blog.getComments().stream()
	            .map(comment -> {
	            	CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
	            	commentDto.setUserId(comment.getUser().getUid());
	            	commentDto.setCommentAuthor(comment.getUser().getName());
	            	return commentDto;})
	            .collect(Collectors.toSet());
	    blogDto.setComments(commentDtos);
		return blogDto;
	}

}
