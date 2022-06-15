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
import com.app.togglev1.dtos.CollaboratorProjectDTO;
import com.app.togglev1.dtos.CreatorProjectDTO;
import com.app.togglev1.dtos.SchoolProjectCardDTO;
import com.app.togglev1.dtos.SchoolProjectDTO;
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
import com.app.togglev1.utils.ServiceMethods;

@RestController
@RequestMapping("/project")
@CrossOrigin
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
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		Set<CollaborationRequest> collaborationRequests = collaborationsRequestService
				.getCollaboratorRequestPendintOfProject(schoolProject.getId());
		if(!collaborationRequests.isEmpty()) {
			for (CollaborationRequest collaboration : collaborationRequests) {
				CollaborationRequestDTO c = new CollaborationRequestDTO();
				c.setId(collaboration.getId());
				c.setIdProject(collaboration.getSchoolProject().getId());
				c.setIdTeacher(collaboration.getSchoolTeacherRequest().getId());
				c.setNameTeacher(collaboration.getSchoolTeacherRequest().getUserNested().getName());
				c.setSend(collaboration.getSended());
				SchoolTeacher schoolTeacher = schoolTeacherService.getOne(collaboration.getSchoolTeacherRequest().getId())
						.get();
				SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
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
	
	@GetMapping("/getCollaboratorRequestPendintOfProject/{id}")
	public ResponseEntity<Set<CollaborationRequestDTO>> getCollaboratorRequestPendintOfProject(@PathVariable("id") long id) {
		SchoolProject schoolProject = schoolProjectService.getOne(id).get();
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		Set<CollaborationRequest> collaborationRequests = collaborationsRequestService
				.getCollaboratorRequestPendintOfProject(schoolProject.getId());
		System.out.println(collaborationRequests.toString());
		if(!collaborationRequests.isEmpty()) {
			for (CollaborationRequest collaboration : collaborationRequests) {
				CollaborationRequestDTO c = new CollaborationRequestDTO();
				c.setId(collaboration.getId());
				c.setIdProject(collaboration.getSchoolProject().getId());
				c.setIdTeacher(collaboration.getSchoolTeacherRequest().getId());
				c.setNameTeacher(collaboration.getSchoolTeacherRequest().getUserNested().getName());
				c.setSend(collaboration.getSended());
				SchoolTeacher schoolTeacher = schoolTeacherService.getOne(collaboration.getSchoolTeacherRequest().getId())
						.get();
				SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
				c.setCitySchool(schoolProfile.getCity());
				c.setNameSchool(schoolProfile.getName());
				c.setCollaborationResponse(collaboration.getCollaborationResponse());
				collaborationRequestDTOs.add(c);
			}
		}
		return new ResponseEntity<Set<CollaborationRequestDTO>>(collaborationRequestDTOs, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<SchoolProjectCardDTO>> getAll() {

		List<SchoolProjectCardDTO> schoolProjectsCardDTO = new ArrayList<>();
		List<SchoolProject> schoolProject = schoolProjectService.getAll();
		for (SchoolProject sp : schoolProject) {
			SchoolProjectCardDTO projectCardDTO = new SchoolProjectCardDTO();
			projectCardDTO.setId(sp.getId());
			projectCardDTO.setCurrentCreate(sp.getCurrentCreate());
			projectCardDTO.setTitle(sp.getTitle());
			projectCardDTO.setDescription(sp.getDescription());
			CreatorProjectDTO creatorProjectDTO = new CreatorProjectDTO();
			SchoolTeacher schoolTeacher = sp.getSchoolTeacherCreator();
			creatorProjectDTO.setNameCreator(schoolTeacher.getUserNested().getName());
			creatorProjectDTO.setNameSchoolCreator(schoolTeacher.getSchoolProfile().getName());
			projectCardDTO.setCreatorProjectDTO(creatorProjectDTO);
			Set<String> nameCycles = ServiceMethods.extractCyclesNames(sp.getListStudiesCycle());
			if (!nameCycles.isEmpty()) {
				projectCardDTO.setNameCycles(nameCycles);
			}
			schoolProjectsCardDTO.add(projectCardDTO);
		}
		return new ResponseEntity<List<SchoolProjectCardDTO>>(schoolProjectsCardDTO, HttpStatus.OK);
	}

	@GetMapping("/getAllBySeeker/{username}")
	public ResponseEntity<List<SchoolProjectCardDTO>> getAllToTeacher(@PathVariable("username") String username) {
		List<SchoolProjectCardDTO> schoolProjectsCardDTO = new ArrayList<>();
		SchoolTeacher schoolTeacherSeeker = schoolTeacherService.getByUserName(username).get();
		List<SchoolProject> schoolProjects = schoolProjectService.getAllDifferent(schoolTeacherSeeker.getId());
		Set<SchoolProject> filterSchoolProject = ServiceMethods.filterCollaborationRequestByTeacher(schoolProjects,
				schoolTeacherSeeker);
		if (!filterSchoolProject.isEmpty()) {
			for (SchoolProject sp : filterSchoolProject) {
				Set<CollaboratorProjectDTO> cPDTOs = ServiceMethods.extractCollaborators(sp);
				if (!ServiceMethods.checkIfExistsCollaborator(cPDTOs, schoolTeacherSeeker)) {
					SchoolProjectCardDTO projectCardDTO = new SchoolProjectCardDTO();
					projectCardDTO.setId(sp.getId());
					projectCardDTO.setCurrentCreate(sp.getCurrentCreate());
					projectCardDTO.setTitle(sp.getTitle());
					projectCardDTO.setDescription(sp.getDescription());
					CreatorProjectDTO creatorProjectDTO = new CreatorProjectDTO();
					SchoolTeacher schoolTeacher = sp.getSchoolTeacherCreator();
					creatorProjectDTO.setNameCreator(schoolTeacher.getUserNested().getUsername());
					creatorProjectDTO.setNameSchoolCreator(schoolTeacher.getSchoolProfile().getName());
					creatorProjectDTO.setCity(schoolTeacher.getSchoolProfile().getCity());
					projectCardDTO.setCreatorProjectDTO(creatorProjectDTO);
					Set<String> nameCycles = ServiceMethods.extractCyclesNames(sp.getListStudiesCycle());
					if (!nameCycles.isEmpty()) {
						projectCardDTO.setNameCycles(nameCycles);
					}
					Set<CollaboratorProjectDTO> collaboratorProjectDTOs = ServiceMethods.extractCollaborators(sp);
					if (!collaboratorProjectDTOs.isEmpty()) {
						projectCardDTO.setCollaboratorProjectDTOs(collaboratorProjectDTOs);
					}
					schoolProjectsCardDTO.add(projectCardDTO);
				}

			}
		}

		return new ResponseEntity<List<SchoolProjectCardDTO>>(schoolProjectsCardDTO, HttpStatus.OK);
	}

	@GetMapping("/getMyCollaborativeProject/{id}")
	public ResponseEntity<List<SchoolProjectCardDTO>> getMyCollaborativeProject(@PathVariable("id") Long id) {
		List<SchoolProjectCardDTO> schoolProjectsCardDTO = new ArrayList<>();
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		Set<SchoolProject> schoolProjects = schoolProjectService.getCollaborativeProject(schoolTeacher);
		for (SchoolProject sp : schoolProjects) {
			SchoolProjectCardDTO projectCardDTO = new SchoolProjectCardDTO();
			projectCardDTO.setId(sp.getId());
			projectCardDTO.setCurrentCreate(sp.getCurrentCreate());
			projectCardDTO.setTitle(sp.getTitle());
			CreatorProjectDTO creatorProjectDTO = new CreatorProjectDTO();
			SchoolTeacher schoolTeacher2 = sp.getSchoolTeacherCreator();
			creatorProjectDTO.setNameCreator(schoolTeacher2.getUserNested().getUsername());
			creatorProjectDTO.setNameSchoolCreator(schoolTeacher2.getSchoolProfile().getName());
			creatorProjectDTO.setCity(schoolTeacher2.getSchoolProfile().getCity());
			projectCardDTO.setCreatorProjectDTO(creatorProjectDTO);
			Set<String> nameCycles = ServiceMethods.extractCyclesNames(sp.getListStudiesCycle());
			if (!nameCycles.isEmpty()) {
				projectCardDTO.setNameCycles(nameCycles);
			}
			schoolProjectsCardDTO.add(projectCardDTO);
		}
		return new ResponseEntity<List<SchoolProjectCardDTO>>(schoolProjectsCardDTO, HttpStatus.OK);
	}

}
