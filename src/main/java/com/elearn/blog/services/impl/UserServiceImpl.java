package com.elearn.blog.services.impl;

import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearn.blog.config.AppConstants;
import com.elearn.blog.entities.Role;
import com.elearn.blog.entities.User;
import com.elearn.blog.payloads.UserDto;
import com.elearn.blog.repositories.RoleRepo;
import com.elearn.blog.repositories.UserRepo;
import com.elearn.blog.services.UserService;
import com.elearn.blog.exceptions.*;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
//	@Override
//	public UserDto createUser(UserDto userDto) {
//		User user=this.dtotoUser(userDto);
//		System.out.println("BarunBarunBarunBarunBarun");
//		User savedUser =this.userRepo.save(user);
//		return this.usertoDto(savedUser);
//	}

	public UserDto updateUser(UserDto userDto, Integer uid) {
		
		User user = this.userRepo.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User","id",uid));
		user.setName(userDto.getName());
		user.setEmail(user.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(user.getPassword());
		user.setPhone(userDto.getPhone());
		
		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.usertoDto(updatedUser);
		return userDto1;
	}
	
	@Override
	public void updatePassword(UserDto userDto, Integer uid) {
		// TODO Auto-generated method stub
		User user = this.userRepo.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User","id",uid));
		user.setPassword(this.passwordEncoder.encode(userDto.getNewPassword()));
		this.userRepo.save(user);
	}

	public UserDto getUserById(Integer uid) {
		
		User user = this.userRepo.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User","id",uid));
		return this.usertoDto(user);
	}

	public List<UserDto> getAllUsers() {
		
		List<User> users =this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.usertoDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	public void deleteUser(Integer uid) {
		// TODO Auto-generated method stub
		User user = this.userRepo.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User","id",uid));
		//this.userRepo.delete(user);
		user.getRoles().clear(); // Remove all associated roles
		userRepo.delete(user); // Delete the user
	}
	
	private User dtotoUser(UserDto userdto) {
		User user = this.modelMapper.map(userdto, User.class);
		/*user.setUid(userdto.getUid());
		user.setName(userdto.getName());
		user.setEmail(userdto.getEmail());
		user.setAbout(userdto.getAbout());
		user.setPassword(userdto.getPassword());
		user.setPhone(userdto.getPhone());*/
		return user;
	}
	
	private UserDto usertoDto(User user) {
		UserDto userdto = this.modelMapper.map(user, UserDto.class);
		/*userdto.setUid(user.getUid());
		userdto.setName(user.getName());
		userdto.setEmail(user.getEmail());
		userdto.setAbout(user.getAbout());
		userdto.setPassword(user.getPassword());
		userdto.setPhone(user.getPhone());*/
		return userdto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user  = this.modelMapper.map(userDto, User.class);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		
		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		
		return this.modelMapper.map(newUser, UserDto.class);
		
	}

	

}
