package com.app.togglev1.security.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.app.togglev1.entities.SchoolProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@DiscriminatorValue("M")
public class UserManager extends BasicUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserManager() {
		super();
	}
	
	public UserManager(String name, String username, String email,
			String password) {
		super(name, username, email, password);
	}

	@JsonBackReference(value="user-manager")
	//@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="userManager")	
	@OneToOne(mappedBy = "userManager", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)	
	private SchoolProfile schoolProfile;

	public SchoolProfile getSchoolProfile() {
		return schoolProfile;
	}

	public void setSchoolProfile(SchoolProfile schoolProfile) {
		this.schoolProfile = schoolProfile;
	}
	
	
	
}
