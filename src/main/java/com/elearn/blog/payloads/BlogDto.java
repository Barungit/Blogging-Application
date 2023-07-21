package com.elearn.blog.payloads;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.elearn.blog.entities.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Setter
@Getter
public class BlogDto {
	private Integer bid;
	@NotBlank
	@Size(min = 5, max = 100)
	private String title;
	@NotBlank
	@Size(min = 300, max = 30000)
	private String content;
	private String picname;
	private Date uploadDate;
	private Long view;
	private CategoryDto category;
	private UserDto user;
	
	private Set<CommentDto> comments = new HashSet<>();
}
