package com.elearn.blog.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearn.blog.exceptions.ApiException;
import com.elearn.blog.payloads.ApiResponse;
import com.elearn.blog.payloads.UserDto;
import com.elearn.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
//	//Create Single User
//	@PostMapping("/")
//	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
//		UserDto createUserDto = this.userService.createUser(userDto);
//		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
//		
//	}
	
	//Update single user
	@PutMapping("/{uid}")
	public ResponseEntity<UserDto> updateUser( @RequestBody UserDto userDto, @PathVariable Integer uid){
		UserDto updatedUser =  this.userService.updateUser(userDto, uid);
		return ResponseEntity.ok(updatedUser);
	}
	
	//update password
	@PutMapping("/password/{uid}")
	public ResponseEntity<ApiResponse> passwordChange(@RequestBody UserDto userDto, @PathVariable Integer uid){
		System.out.println("Barun ji");
		System.out.println(userDto.getEmail());
		System.out.println(userDto.getPassword());
		System.out.println(userDto.getNewPassword());
		System.out.println("Barun ji");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword());
		try {
			this.authenticationManager.authenticate(authenticationToken);
			System.out.println("Good details");
			this.userService.updatePassword(userDto, uid);
			 
		} catch (BadCredentialsException e) {
			System.out.println("Invalid Details !!");
			throw new ApiException("Invalid username or password !!");
		}
		
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Password Changed successfully", true), HttpStatus.OK);
	}
	
	//Delete single user
//	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{uid}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("uid") Integer uid){
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
	}
	
	//Get all Users
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	//Get Single user
	@GetMapping("/{uid}")
	public ResponseEntity<UserDto> getSingleUsers(@PathVariable Integer uid){
		return ResponseEntity.ok(this.userService.getUserById(uid));
	}
}
