package com.elearn.blog.payloads;

import java.util.Date;

import com.elearn.blog.entities.Blog;
import com.elearn.blog.entities.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
@Getter	

@Setter
public class CommentDto {
	
	private int id;
	
	private String content;
	
	private Date commentDate;
	
	//private Blog blog;
	
	//private User user;
}
