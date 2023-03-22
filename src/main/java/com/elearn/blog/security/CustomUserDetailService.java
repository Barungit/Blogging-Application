package com.elearn.blog.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elearn.blog.entities.User;
import com.elearn.blog.exceptions.ResourceNotFoundException;
import com.elearn.blog.repositories.UserRepo;
@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// loading user from database by username
		User user = this.userRepo.email(username).orElseThrow(() -> new ResourceNotFoundException("User", "username",username));
		return user;
		
	}
	
	

}
