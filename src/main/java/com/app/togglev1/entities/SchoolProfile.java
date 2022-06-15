package com.app.togglev1.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.app.togglev1.security.entities.UserManager;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class SchoolProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NonNull
	@Column(name="schoolName", unique=true, length = 50)
	private String name;
	
	@NonNull
	@Column(name="schoolCode", unique=true, length = 8)
	private Integer code;
	
	@NonNull
	@Column(length = 50)
	private String city;
	
	@NonNull
	@Column(columnDefinition = "varchar(280)")
	private String description;
	
	
	@JsonManagedReference(value="user-manager")
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_manager_id", referencedColumnName = "id", nullable = false)
	private UserManager userManager;
	
	@JsonManagedReference(value="teacher")
	@OneToMany(fetch=FetchType.EAGER, mappedBy = "schoolProfile")
	private Set<SchoolTeacher>  schoolTeachers = new HashSet<>();
	


	@NonNull
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "studies_cycle_school_profile", joinColumns = @JoinColumn(name="school_profile_id"),
	inverseJoinColumns = @JoinColumn(name="studies_cycle_id"))
	private Set <StudiesCycle> listStudiesCycle = new HashSet<>(); 
	

}
