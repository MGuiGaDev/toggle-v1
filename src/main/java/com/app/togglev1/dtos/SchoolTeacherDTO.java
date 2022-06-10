package com.app.togglev1.dtos;

import java.io.Serializable;
import java.util.Set;

import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.StudiesCycle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchoolTeacherDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String name;
	String username;
	String email;
	String password;
	Set <SchoolProject> schoolProjectsCreator;
	Set <SchoolProject> schoolProjects;
	Set <StudiesCycle> listStudiesCycle;
	String schoolProfileName;
	long idSchoolProfile;
	
	
}
