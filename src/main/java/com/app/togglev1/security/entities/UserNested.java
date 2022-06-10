package com.app.togglev1.security.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.app.togglev1.entities.SchoolTeacher;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@DiscriminatorValue("N")
public class UserNested extends BasicUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNested() {
		super();
	}
	
	public UserNested(String name, String username, String email,
			String password) {
		super(name, username, email, password);
	}

	@JsonBackReference(value="user-nested")
	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="userNested")
	@OneToOne(mappedBy = "userNested", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)	
	private SchoolTeacher schoolTeacher;

	public SchoolTeacher getSchoolTeacher() {
		return schoolTeacher;
	}

	public void setSchoolTeacher(SchoolTeacher schoolTeacher) {
		this.schoolTeacher = schoolTeacher;
	}
	
}
