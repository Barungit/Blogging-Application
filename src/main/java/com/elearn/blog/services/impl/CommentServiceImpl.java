package com.elearn.blog.services.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearn.blog.entities.Blog;
import com.elearn.blog.entities.Comment;
import com.elearn.blog.entities.User;
import com.elearn.blog.exceptions.ResourceNotFoundException;
import com.elearn.blog.payloads.CommentDto;
import com.elearn.blog.repositories.BlogRepo;
import com.elearn.blog.repositories.CommentRepo;
import com.elearn.blog.repositories.UserRepo;
import com.elearn.blog.services.CommentService;
@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private BlogRepo blogRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto,Integer bid, Integer uid) {
		Blog blog = this.blogRepo.findById(bid).orElseThrow(()-> new ResourceNotFoundException("Blog", "Blog Id", bid));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		User user = this.userRepo.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", uid));
		comment.setBlog(blog);
		comment.setCommentDate(new Date());
		
		comment.setUser(user);
		Comment savedComment = this.commentRepo.save(comment);
		
		CommentDto reply =this.modelMapper.map(savedComment, CommentDto.class);
		reply.setUserId(uid);
		reply.setCommentAuthor(user.getName());
		return reply;
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "Comment ID", commentId));
		this.commentRepo.delete(comment);

	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "Comment ID", commentId));
		Comment com = this.modelMapper.map(commentDto, Comment.class);
		comment.setContent(com.getContent());
		Comment newComment = this.commentRepo.save(comment);
		return this.modelMapper.map(newComment, CommentDto.class);
	}

}
