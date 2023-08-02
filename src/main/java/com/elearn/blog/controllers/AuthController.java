package com.elearn.blog.controllers;

import java.util.UUID;

import org.hibernate.mapping.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearn.blog.entities.User;
import com.elearn.blog.exceptions.ApiException;
import com.elearn.blog.exceptions.ResourceNotFoundException;
import com.elearn.blog.payloads.ApiResponse;
import com.elearn.blog.payloads.JwtAuthRequest;
import com.elearn.blog.payloads.JwtAuthResponse;
import com.elearn.blog.payloads.UserDto;
import com.elearn.blog.repositories.UserRepo;
import com.elearn.blog.security.JwtTokenHelper;
import com.elearn.blog.services.EmailService;
import com.elearn.blog.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmailService emailService;
	
	
	@PostMapping("login/")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		this.authenticate(request.getEmail(), request.getPassword());
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.modelMapper.map((User)userDetails, UserDto.class));
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	private void authenticate(String email, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid Details !!");
			throw new ApiException("Invalid username or password !!");
		}
	}
	
	//register new user api
	@PostMapping("register/")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
	}
	
	//forgot password
	@PostMapping("fp/{email}")
	public  ResponseEntity<ApiResponse> forgotPassword(@PathVariable("email") String email){
		System.out.println(email);
		String subject = "Password reset link :";
		User user = this.userRepo.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            this.userRepo.save(user);
            //User user2 = this.userRepo.findByresetToken(token);
            //System.out.println(user2.getAbout());
            String resetLink ="http://localhost:3000/forgot-password/" + token;
            emailService.sendEmail(email, subject, resetLink);
        }else {
        	System.out.println("Invalid Details !!");
			throw new ApiException("User with provided Email not found!!");

        }
//        // Redirect to a confirmation page
//        return "redirect:/forgot-password-confirmation";
//    }
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Email sent!", true), HttpStatus.OK);
	}
}
