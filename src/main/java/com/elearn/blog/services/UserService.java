package com.elearn.blog.services;

import java.util.List;

import com.elearn.blog.payloads.UserDto;

public interface UserService {
	
	UserDto registerNewUser(UserDto user);
	
//	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto user, Integer uid);
	void updatePassword(UserDto user, Integer uid);
	UserDto getUserById(Integer userId);
	List<UserDto> getAllUsers();
	void deleteUser(Integer uid);
	void forgotPassword(String token, String pass);
}
