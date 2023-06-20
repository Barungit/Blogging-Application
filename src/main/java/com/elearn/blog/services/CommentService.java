package com.elearn.blog.services;

import com.elearn.blog.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto,Integer bid, Integer uid);
	
	void deleteComment(Integer commentId);
	
	CommentDto updateComment(CommentDto commentDto,Integer commentId);
	
	
}
