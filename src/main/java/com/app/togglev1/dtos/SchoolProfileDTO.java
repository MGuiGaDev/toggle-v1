package com.app.togglev1.dtos;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.security.entities.UserManager;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchoolProfileDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private long id;
	
	@NotBlank
	private String name;
	@NotBlank
	private String city;
	@Email
	private String description;
	@NotBlank
	private String password;
	
	private UserManager userManager;
	private Set <StudiesCycle> listStudiesCycle = new HashSet<>(); 
	private Set <SchoolTeacher> listTeachers = new HashSet<>();
	
}
