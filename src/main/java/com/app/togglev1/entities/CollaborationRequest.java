package com.app.togglev1.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.app.togglev1.enums.CollaborationResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CollaborationRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@JsonBackReference(value="collaboration-request-teacher")
	@ManyToOne()
	@JoinColumn(name = "schoolTeacherRequest_id", referencedColumnName = "id", nullable = false)
	private SchoolTeacher schoolTeacherRequest;
	
	@JsonBackReference(value="collaboration-request-project")
	@ManyToOne()
	@JoinColumn(name = "schoolProject_id", referencedColumnName = "id", nullable = false)
	private SchoolProject schoolProject;
	
	private Date sended;
	
	@Column(columnDefinition = "varchar(280)")
	private String message;
	
	@Column(columnDefinition = "varchar(7)")
	@Enumerated(EnumType.STRING)
	private CollaborationResponse collaborationResponse;
	
}
