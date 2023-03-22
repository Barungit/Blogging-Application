package com.elearn.blog.payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {
	
	private String email ;
	
	private String password;
	
}
