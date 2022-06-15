package com.app.togglev1.entities;

import java.io.Serializable;
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
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;

import com.app.togglev1.security.entities.UserNested;
import com.app.togglev1.services.SchoolProjectService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

//Aquí sucedión algo con la circularidad por Lombok...
//Se solucionó cambiando @Data por @Getter and @Setter
//https://stackoverflow.com/questions/34972895/lombok-hashcode-issue-with-java-lang-stackoverflowerror-null
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SchoolTeacher implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@JsonBackReference(value="teacher")
	@ManyToOne()
	@JoinColumn(name = "schoolProfile_id", referencedColumnName = "id", nullable = false)
	private SchoolProfile schoolProfile;
	
	@JsonManagedReference(value="proyect-creator")
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "schoolTeacherCreator", cascade = CascadeType.ALL)
	private Set<SchoolProject>  schoolProyect;
	
	@NonNull
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "studies_cycle_teacher_profile", joinColumns = @JoinColumn(name="teacher_id"),
	inverseJoinColumns = @JoinColumn(name="teacher_cycle_id"))
	private Set <StudiesCycle> listStudiesCycle = new HashSet<>(); 
	
	@JsonManagedReference(value="user-nested")
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_nested_id", referencedColumnName = "id", nullable = false)
	private UserNested userNested;
	
	
	@JsonManagedReference(value="collaboration-request-teacher")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "schoolTeacherRequest")
	private Set<CollaborationRequest> collaborationRequests  = new HashSet<>();
	
	@ManyToMany(mappedBy = "collaborators")
    private Set<SchoolProject> schoolProjects;
	
}
