package com.elearn.blog.controllers;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elearn.blog.config.AppConstants;
import com.elearn.blog.payloads.ApiResponse;
import com.elearn.blog.payloads.BlogDto;
import com.elearn.blog.payloads.BlogResponse;
import com.elearn.blog.services.BlogService;
import com.elearn.blog.services.FileService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	//create
	/*@PostMapping("/user/{userId}/category/{categoryId}/blogs")
	public ResponseEntity<BlogDto> createBlog(
			@Valid
			@ModelAttribute BlogDto blogDto,
			@PathVariable Integer userId,
			@PathVariable Integer categoryId,
			 @RequestParam("image") MultipartFile image) throws IOException{
			
			BlogDto createdBlog = this.blogService.createBlog(blogDto, userId, categoryId);
			
			 String filename = this.fileService.uploadImage(path, image);
			 createdBlog.setPicname(filename);
			 BlogDto updatedblog = this.blogService.updateBlog(createdBlog, createdBlog.getBid());
			
			 
			return new ResponseEntity<BlogDto>(updatedblog, HttpStatus.CREATED);
	}*/
	@PostMapping("/user/{userId}/category/{categoryId}/blogs")
	public ResponseEntity<BlogDto> createBlog(
			@Valid
			@RequestBody BlogDto blogDto,
			@PathVariable Integer userId,
			@PathVariable Integer categoryId){
			
			BlogDto createdBlog = this.blogService.createBlog(blogDto, userId, categoryId);
			System.out.println("Create blog controller");
			return new ResponseEntity<BlogDto>(createdBlog, HttpStatus.CREATED);
	}
	
	//get blog by bid
	@GetMapping("/blogs/{bid}")
	public ResponseEntity<BlogDto> getBlogByBlogId(@PathVariable Integer bid){
		BlogDto blogDto = this.blogService.getBlogbyId(bid);
		return new ResponseEntity<BlogDto>(blogDto,HttpStatus.OK);
	}
	
	//get all blogs 
	@GetMapping("/blogs")
	public ResponseEntity<BlogResponse> getallBlogs(
			@RequestParam( value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam( value = "pageSize",defaultValue =AppConstants.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam( value = "sortBy",defaultValue =AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam( value = "sortDir",defaultValue =AppConstants.SORT_DIR, required = false) String sortDir
			){
		 BlogResponse allblogs = this.blogService.getallBlogs(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<BlogResponse>(allblogs, HttpStatus.OK);
	}
	
	
	//get blogs by user
	 @GetMapping("/user/{userId}/blogs")
	public ResponseEntity<BlogResponse> getBlogByUser(@PathVariable Integer userId,
			@RequestParam( value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam( value = "pageSize",defaultValue =AppConstants.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam( value = "sortBy",defaultValue =AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam( value = "sortDir",defaultValue =AppConstants.SORT_DIR, required = false) String sortDir){
		 BlogResponse blogs = this.blogService.getallBlogsByUser(userId,pageNumber,pageSize,sortBy,sortDir);
		 return new ResponseEntity<BlogResponse>(blogs, HttpStatus.OK);
	 }
	
	//get blogs by category
		 @GetMapping("/category/{cid}/blogs")
		public ResponseEntity<BlogResponse> getBlogByCategory(@PathVariable Integer cid,
				@RequestParam( value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
				@RequestParam( value = "pageSize",defaultValue =AppConstants.PAGE_SIZE,required = false) Integer pageSize,
				@RequestParam( value = "sortBy",defaultValue =AppConstants.SORT_BY, required = false) String sortBy,
				@RequestParam( value = "sortDir",defaultValue =AppConstants.SORT_DIR, required = false) String sortDir){
			 BlogResponse blogs = this.blogService.getallBlogsByCategory(cid,pageNumber,pageSize,sortBy,sortDir);
			 return new ResponseEntity<BlogResponse>(blogs, HttpStatus.OK);
		 }
		//search by blog string
		 @GetMapping("/blogs/search/{keywords}")
			public ResponseEntity<BlogResponse> searchBlogByTitle(@PathVariable("keywords") String keywords,
					@RequestParam( value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
					@RequestParam( value = "pageSize",defaultValue =AppConstants.PAGE_SIZE,required = false) Integer pageSize,
					@RequestParam( value = "sortBy",defaultValue =AppConstants.SORT_BY, required = false) String sortBy,
					@RequestParam( value = "sortDir",defaultValue =AppConstants.SORT_DIR, required = false) String sortDir){
				 BlogResponse blogs = this.blogService.searchBlogs(keywords, pageNumber, pageSize, sortBy, sortDir);
				 return new ResponseEntity<BlogResponse>(blogs, HttpStatus.OK);
			 }
		 
		 
	 //delete blog by id
		 @DeleteMapping("/blogs/{bid}")
		 public ApiResponse deleteBlog(@PathVariable Integer bid) {
			 this.blogService.deleteBlog(bid);
			 return new ApiResponse("Blog is successfully deleted!", true);
		 }
		 
		 //update blog by id
		 @PutMapping("/blogs/{bid}")
		 public ResponseEntity<BlogDto> updateBlog(@RequestBody BlogDto blogDto, @PathVariable Integer bid){
			 
			 BlogDto updatedBlog = this.blogService.updateBlog(blogDto, bid);
			 return new ResponseEntity<BlogDto>(updatedBlog, HttpStatus.OK);
		 }
		 
		 //upload image
		 @PostMapping("/blogs/image/upload/{bid}")
		 public ResponseEntity<BlogDto> uploadBlogImage(
				 @PathVariable Integer bid,
				 @RequestParam("image") MultipartFile image) throws IOException{
			 System.out.println("Create blog upload pic");
			 BlogDto blogDto = this.blogService.getBlogbyId(bid);
			 String filename = this.fileService.uploadImage(path, image);
			 blogDto.setPicname(filename);
			 BlogDto updatedblog = this.blogService.updateBlog(blogDto, bid);
			 return new ResponseEntity<BlogDto>(updatedblog,HttpStatus.OK);
		 }
		//serve image
		 @GetMapping(value = "/blogs/image/{picName}",produces = MediaType.IMAGE_JPEG_VALUE)
		 public void downloadImage(
				 @PathVariable("picName") String picName,
				 HttpServletResponse response) throws IOException{
			 System.out.println("Create blog serve image");
			 System.out.println(picName);
			 InputStream resource = this.fileService.getResource(path, picName);
			 response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			 StreamUtils.copy(resource, response.getOutputStream());
		 }
		 
	
}
