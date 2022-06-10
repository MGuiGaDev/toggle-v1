package com.app.togglev1.security.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUser {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
}
