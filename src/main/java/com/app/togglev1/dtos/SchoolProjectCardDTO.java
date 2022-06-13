package com.app.togglev1.dtos;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchoolProjectCardDTO {
	
	private long id;
	private String title;
	private String description;
	private CreatorProjectDTO creatorProjectDTO;
	private Set<CollaboratorProjectDTO>collaboratorProjectDTOs;
	private Set<CollaborationRequestDTO>collaboratorRequestDTOs;
	private Set<String> nameCycles;
	private Date currentCreate;
	

}
