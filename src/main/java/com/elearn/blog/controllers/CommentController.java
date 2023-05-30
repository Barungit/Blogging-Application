package com.elearn.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearn.blog.entities.Comment;
import com.elearn.blog.payloads.ApiResponse;
import com.elearn.blog.payloads.CommentDto;
import com.elearn.blog.services.CommentService;
@RestController
@RequestMapping("/api/v1/")
public class CommentController {
	
	@Autowired
	private CommentService commservice;
	@PostMapping("/blog/{bid}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer bid) {
		CommentDto comment = this.commservice.createComment(commentDto, bid);
		return new ResponseEntity<CommentDto>(comment,HttpStatus.CREATED);
	}
	
	//cdelete Comment
	@DeleteMapping("/comments/{cid}")
	public ResponseEntity<ApiResponse> Comment(@PathVariable Integer cid){
		this.commservice.deleteComment(cid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment is Deleted Successfully!!", true),HttpStatus.OK);
	}
	
	//update comment
	@PutMapping("/comments/{cid}")
	public ResponseEntity<ApiResponse> updateComment(@RequestBody CommentDto commentDto, @PathVariable Integer cid ){
		CommentDto comment = this.commservice.updateComment(commentDto, cid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment is updated Successfully!!", true),HttpStatus.OK);
	}
}
