package com.elearn.blog.payloads;
import java.util.HashSet;
import java.util.Set;

import com.elearn.blog.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int uid;
	
	@NotEmpty
	@Size(min = 4, message = "Username must be of aleast 4 characters.")
	private String name;
	@NotEmpty
	@jakarta.validation.constraints.Email(message = "Email address is not valid!")
	private String Email;
	@NotEmpty
	private String about;
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	@NotEmpty
	@Size(min = 8,max = 20, message = "Password should be in between 8 to 20 characters & must contain atleast one small alphabet,one capital aplhabet and a number.")
	@Pattern(regexp = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\\s]{8,}$")
	private String password;
	@Pattern(regexp = "^([+]\\d{2}[ ])?\\d{10}$", message = "Must be of 10 characters")
	private String phone;
	
	private String propic;
	
	private Set<RoleDto> roles = new HashSet<>();
	
	private String newPassword;
}
