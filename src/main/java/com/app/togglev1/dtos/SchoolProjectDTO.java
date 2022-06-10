package com.app.togglev1.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchoolProjectDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String title;
	private String description;
	private SchoolTeacherDTO schoolTeacherDTO;
	private long idCreator;
	private StudiesCycle defaultCycle;
	private Set<SchoolTeacher> schoolTeachers;
	private Set<StudiesCycle> listStudiesCycle;
	private Set<CollaborationRequestDTO> collaborationRequestsDTO;
	private Date currentCreate;
	
}
