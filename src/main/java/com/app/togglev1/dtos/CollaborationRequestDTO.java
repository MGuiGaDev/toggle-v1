package com.app.togglev1.dtos;

import java.io.Serializable;
import java.util.Date;

import com.app.togglev1.enums.CollaborationResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollaborationRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long idProject;
	private long idTeacher;
	private String titleProject;
	private String nameTeacher;
	private String nameSchool;
	private String citySchool;
	private Date send;
	private String message;
	private CollaborationResponse collaborationResponse;

}
