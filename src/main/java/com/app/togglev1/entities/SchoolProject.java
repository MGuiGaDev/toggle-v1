package com.app.togglev1.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class SchoolProject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NonNull
	private String title;
	
	@NonNull
	private String description;
	
	@JsonBackReference(value="proyect-creator")
	@ManyToOne()
	@JoinColumn(name = "schoolTeacherCreator_id", referencedColumnName = "id", nullable = false)
	private SchoolTeacher schoolTeacherCreator;
/*
	@JsonBackReference(value="proyect-collaborator")
	@ManyToMany()
	@JoinTable(name = "school_project_collaborator_collaborator", joinColumns = @JoinColumn(name="school_project_id"),
	inverseJoinColumns = @JoinColumn(name="school_teacher_id"))
	private Set <SchoolTeacher> schoolTeachers = new HashSet<>(); 
	*/
	/*
	 
	@JsonBackReference
	@ManyToMany(mappedBy = "schoolProjects")
	private Set <SchoolTeacher> schoolTeachers = new HashSet<>(); 
	
	@Fetch(value = FetchMode.SELECT)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "forumAnswerId")
    @JsonManagedReference
    private Set <SchoolTeacher> schoolTeachers = new HashSet<>(); 
	*/
	@JsonBackReference
	@OneToMany(
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	 @JoinTable (
		        name="school_project_collaborators",
		        joinColumns={ @JoinColumn(name="school_project_id") },
		        inverseJoinColumns={ @JoinColumn(name="school_teacher_id", unique=true) }
		    )
	private Set<SchoolTeacher> collaborators = new HashSet<>();
	
	@ManyToMany()
	@JoinTable(name = "studies_cycle_school_project", joinColumns = @JoinColumn(name="school_project_id"),
	inverseJoinColumns = @JoinColumn(name="studies_cycle_id"))
	private Set <StudiesCycle> listStudiesCycle = new HashSet<>(); 
	
	@JsonManagedReference(value="collaboration-request-project")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE) //Esto lo tenía que utilizar porque el JPA no permite
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "schoolProject")
	private Set<CollaborationRequest> collaborationRequests  = new HashSet<>();
	
	private Date currentCreate;


}
