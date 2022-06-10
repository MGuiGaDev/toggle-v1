package com.app.togglev1.security.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	@NotBlank
	private String name;
	@NotBlank
	private String username;
	@Email
	private String email;
	@NotBlank
	private String password;
	
	private String passwordEncode;
	
	private Set<String> roles = new HashSet<>();
	
	private SchoolProfile schoolProfile;
	
	private SchoolTeacher schoolTeacher;
	
	private Long idSchoolProfile;
	
}
