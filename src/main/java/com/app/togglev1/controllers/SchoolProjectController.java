package com.app.togglev1.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.dtos.CollaborationRequestDTO;
import com.app.togglev1.dtos.SchoolProjectDTO;
import com.app.togglev1.dtos.SchoolTeacherDTO;
import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.enums.CollaborationResponse;
import com.app.togglev1.security.services.BasicUserService;
import com.app.togglev1.services.CollaborationsRequestService;
import com.app.togglev1.services.SchoolProfileService;
import com.app.togglev1.services.SchoolProjectService;
import com.app.togglev1.services.SchoolTeacherService;
import com.app.togglev1.services.StudiesService;

@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:4200")
public class SchoolProjectController {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	BasicUserService basicUserService;

	@Autowired
	SchoolTeacherService schoolTeacherService;

	@Autowired
	SchoolProfileService schoolProfileService;

	@Autowired
	StudiesService studiesService;

	@Autowired
	SchoolProjectService schoolProjectService;

	@Autowired
	CollaborationsRequestService collaborationsRequestService;

	@GetMapping("/getOne/{title}")
	public ResponseEntity<SchoolProject> getProjectByTitle(@PathVariable("title") String title) {
		SchoolProject schoolProject = schoolProjectService.getByTitle(title).get();
		return new ResponseEntity<SchoolProject>(schoolProject, HttpStatus.OK);
	}

	@GetMapping("/getOneId/{id}")
	public ResponseEntity<SchoolProjectDTO> getProjectById(@PathVariable("id") long id) {
		SchoolProject schoolProject = schoolProjectService.getOne(id).get();
		Set<CollaborationRequest> collaborationRequests = collaborationsRequestService
				.getAllBySchoolProyect(schoolProject);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for (CollaborationRequest collaboration : collaborationRequests) {
			if (collaboration.getCollaborationResponse().equals(CollaborationResponse.PENDINT)) {
				CollaborationRequestDTO c = new CollaborationRequestDTO();
				c.setId(collaboration.getId());
				c.setIdProject(collaboration.getSchoolProject().getId());
				c.setIdTeacher(collaboration.getSchoolTeacherRequest().getId());
				c.setNameTeacher(collaboration.getSchoolTeacherRequest().getUserNested().getName());
				c.setSend(collaboration.getSended());
				SchoolTeacher schoolTeacher = schoolTeacherService
						.getOne(collaboration.getSchoolTeacherRequest().getId()).get();
				SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId())
						.get();
				c.setCitySchool(schoolProfile.getCity());
				c.setNameSchool(schoolProfile.getName());
				collaborationRequestDTOs.add(c);
			}
		}
		SchoolProjectDTO schoolProjectDTO = new SchoolProjectDTO();
		schoolProjectDTO.setId(schoolProject.getId());
		schoolProjectDTO.setIdCreator(schoolProject.getSchoolTeacherCreator().getId());
		schoolProjectDTO.setDescription(schoolProject.getDescription());
		schoolProjectDTO.setTitle(schoolProject.getTitle());
		schoolProjectDTO.setListStudiesCycle(schoolProject.getListStudiesCycle());
		schoolProjectDTO.setCollaborationRequestsDTO(collaborationRequestDTOs);
		schoolProjectDTO.setCurrentCreate(schoolProject.getCurrentCreate());
		schoolProjectDTO.setSchoolTeachers(schoolProject.getCollaborators());
		return new ResponseEntity<SchoolProjectDTO>(schoolProjectDTO, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<SchoolProjectDTO>> getAll() {
		List<SchoolProject> schoolProjects = schoolProjectService.getAll();
		List<SchoolProjectDTO> schoolProjectDTO = new ArrayList<>();
		for(SchoolProject sp : schoolProjects) {
			SchoolProjectDTO projectDTO = new SchoolProjectDTO();
			projectDTO.setCurrentCreate(sp.getCurrentCreate());
			projectDTO.setTitle(sp.getTitle());
			projectDTO.setDescription(sp.getDescription());
			SchoolTeacherDTO schoolTeacherDTO = new SchoolTeacherDTO();
			SchoolTeacher schoolTeacher = sp.getSchoolTeacherCreator();
			schoolTeacherDTO.setName(schoolTeacher.getUserNested().getName());
			projectDTO.setSchoolTeacherDTO(schoolTeacherDTO);
			schoolProjectDTO.add(projectDTO);
		}
		return new ResponseEntity<List<SchoolProjectDTO>>(schoolProjectDTO, HttpStatus.OK);
	}
}
