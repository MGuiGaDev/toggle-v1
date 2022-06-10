package com.app.togglev1.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.dtos.CollaborationRequestDTO;
import com.app.togglev1.dtos.Mensaje;
import com.app.togglev1.dtos.SchoolProjectDTO;
import com.app.togglev1.dtos.SchoolTeacherDTO;
import com.app.togglev1.dtos.StudiesDTO;
import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.enums.CollaborationResponse;
import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.services.BasicUserService;
import com.app.togglev1.services.CollaborationsRequestService;
import com.app.togglev1.services.SchoolProfileService;
import com.app.togglev1.services.SchoolProjectService;
import com.app.togglev1.services.SchoolTeacherService;
import com.app.togglev1.services.StudiesService;

@RestController
@RequestMapping("/teacher")
@CrossOrigin(origins = "http://localhost:4200")
public class SchoolTeacherController {

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

	@GetMapping("/detail/{username}")
	public ResponseEntity<?> getByUserName(@PathVariable("username") String username) {
		if (!basicUserService.existsByUserName(username))
			return new ResponseEntity<Mensaje>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
		BasicUser basicUser = basicUserService.getByUserName(username).get();
		SchoolTeacher schoolTeacher = schoolTeacherService.getByUserNestedId(basicUser.getId()).get();
		SchoolTeacherDTO schoolTeacherDTO = new SchoolTeacherDTO();
		schoolTeacherDTO.setId(schoolTeacher.getId());
		schoolTeacherDTO.setSchoolProjectsCreator(schoolTeacher.getSchoolProyect());
		schoolTeacherDTO.setName(schoolTeacher.getUserNested().getName());
		schoolTeacherDTO.setEmail(schoolTeacher.getUserNested().getEmail());
		schoolTeacherDTO.setPassword(schoolTeacher.getUserNested().getPassword());
		schoolTeacherDTO.setListStudiesCycle(schoolTeacher.getListStudiesCycle());
		//schoolTeacherDTO.setSchoolProjects(schoolTeacher.getSchoolProjects());
		SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
		schoolTeacherDTO.setSchoolProfileName(schoolProfile.getName());
		schoolTeacherDTO.setIdSchoolProfile(schoolProfile.getId());
		return new ResponseEntity<SchoolTeacherDTO>(schoolTeacherDTO, HttpStatus.OK);
	}
	
	@PutMapping("/updateTeacher/{id}")
	public ResponseEntity<Mensaje> updateCycle(@PathVariable("id") long id,
			@RequestBody SchoolTeacherDTO newUser) {
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(newUser.getId()).get();
		BasicUser basicUser = basicUserService.getByUserName(schoolTeacher.getUserNested().getUsername()).get();
		basicUser.setEmail(newUser.getEmail());
		basicUser.setName(newUser.getName());
		basicUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		basicUserService.save(basicUser);
		return new ResponseEntity<Mensaje>(new Mensaje("Perfil actualizado"), HttpStatus.OK);
	}
	
	@PutMapping("/addCreatorProject/{id}")
	public ResponseEntity<Mensaje> addCreator(@PathVariable("id") long id,
			@RequestBody SchoolProjectDTO schoolProject) {
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		SchoolProject project = new SchoolProject();
		project.setDescription(schoolProject.getDescription());
		project.setTitle(schoolProject.getTitle());
		project.setSchoolTeacherCreator(schoolTeacher);
		project.setCurrentCreate(new Date());
		schoolProjectService.save(project);
		return new ResponseEntity<Mensaje>(new Mensaje("Perfil actualizado"), HttpStatus.OK);
	}
	
	@PutMapping("/addCycleToProyect/{id}")
	public ResponseEntity<Mensaje> addCycleToProyect(@PathVariable("id") long id,
			@RequestBody StudiesDTO studiesDTO) {
		SchoolProject project = schoolProjectService.getOne(id).get();
		StudiesCycle studiesCycle = studiesService.getOne(studiesDTO.getCode());
		Set <StudiesCycle> studiesCycles = project.getListStudiesCycle();
		studiesCycles.add(studiesCycle);
		project.setListStudiesCycle(studiesCycles);
		schoolProjectService.save(project);
		return new ResponseEntity<Mensaje>(new Mensaje("Proyecto actualizado"), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteCycleOfProyect/{id}")
	public ResponseEntity<Mensaje> deleteCycleOfProyect(@PathVariable("id") long id,
			@RequestBody StudiesDTO studiesDTO) {
		SchoolProject project = schoolProjectService.getOne(id).get();
		StudiesCycle studiesCycle = studiesService.getOne(studiesDTO.getCode());
		project.getListStudiesCycle().remove(studiesCycle);
		schoolProjectService.save(project);
		return new ResponseEntity<Mensaje>(new Mensaje("Proyecto actualizado"), HttpStatus.OK);
	}

	@GetMapping("/getProject/{title}")
	public ResponseEntity<SchoolProjectDTO> getProjectByTitle(@PathVariable("title") String title) {
		SchoolProject schoolProject = schoolProjectService.getByTitle(title).get();	
		Set<CollaborationRequest> collaborationRequests = collaborationsRequestService.getAllBySchoolProyect(schoolProject);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for(CollaborationRequest collaboration: collaborationRequests) {
			CollaborationRequestDTO c = new CollaborationRequestDTO();
			c.setId(collaboration.getId());
			c.setIdTeacher(collaboration.getSchoolTeacherRequest().getId());
			c.setNameTeacher(collaboration.getSchoolTeacherRequest().getUserNested().getName());
			SchoolTeacher schoolTeacher = schoolTeacherService.getOne(collaboration.getSchoolTeacherRequest().getId()).get();
			SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
			c.setCitySchool(schoolProfile.getCity());
			c.setNameSchool(schoolProfile.getName());
			collaborationRequestDTOs.add(c);
		}
		SchoolProjectDTO schoolProjectDTO = new SchoolProjectDTO();
		schoolProjectDTO.setId(schoolProject.getId());
		schoolProjectDTO.setIdCreator(schoolProject.getSchoolTeacherCreator().getId());
		schoolProjectDTO.setDescription(schoolProject.getDescription());
		schoolProjectDTO.setTitle(schoolProject.getTitle());
		schoolProjectDTO.setListStudiesCycle(schoolProject.getListStudiesCycle());
		schoolProjectDTO.setCollaborationRequestsDTO(collaborationRequestDTOs);
		return new ResponseEntity<SchoolProjectDTO>(schoolProjectDTO, HttpStatus.OK);
	}
	
	@PutMapping("/updateCycle/{code}")
	public ResponseEntity<Mensaje> updateCycle(@PathVariable("code") String code,
			@RequestBody SchoolTeacherDTO newUser) {
		StudiesCycle cycle = new StudiesCycle();
		Set <StudiesCycle> studiesCycles = new HashSet<>();
		studiesCycles = newUser.getListStudiesCycle();
		cycle = studiesService.getOne(code);
		studiesCycles.add(cycle);
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(newUser.getId()).get();
		schoolTeacher.setListStudiesCycle(studiesCycles);
		schoolTeacherService.save(schoolTeacher);
		return new ResponseEntity<Mensaje>(new Mensaje("Ciclo añadido"), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteCycle/{id}")
	public void deleteCycle(@PathVariable("id") long id, @RequestBody StudiesDTO cycle) {
		
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		Set <StudiesCycle> studiesCycles = new HashSet<>();
		studiesCycles = schoolTeacher.getListStudiesCycle();
		Iterator <StudiesCycle> ite = studiesCycles.iterator();
		while(ite.hasNext()) {
			StudiesCycle studiesCycle = ite.next();
			if(studiesCycle.getCode().equals(cycle.getCode())){
				ite.remove();
			}
		}
		schoolTeacher.setListStudiesCycle(studiesCycles);
		schoolTeacherService.save(schoolTeacher);
	}
	
	@DeleteMapping("/deleteProject/{id}")
	public void deleteProject(@PathVariable("id") long id) {
		SchoolProject schoolProject = schoolProjectService.getOne(id).get();
		schoolProjectService.delete(schoolProject);
	}
	
	
	@PostMapping("/addCollaborationRequest/{id}")
	public void addCollaborationRequest(@PathVariable("id") long id, @RequestBody SchoolTeacher schoolTeacher) {
		SchoolProject schoolProject = schoolProjectService.getOne(id).get();
		SchoolTeacher schoolTeacher2 = schoolTeacherService.getOne(schoolTeacher.getId()).get();
		CollaborationRequest collaborationRequest = new CollaborationRequest();
		collaborationRequest.setSchoolProject(schoolProject);
		collaborationRequest.setSchoolTeacherRequest(schoolTeacher2);
		collaborationRequest.setSended(new Date());
		collaborationRequest.setCollaborationResponse(CollaborationResponse.PENDINT);
		collaborationsRequestService.save(collaborationRequest);
	}
	
	@PutMapping("/aceptCollaborationRequest/{id}")
	public void aceptCollaborationRequest(@PathVariable("id") long id,
			@RequestBody SchoolProject project) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationRequest.setCollaborationResponse(CollaborationResponse.ACEPTED);
		collaborationsRequestService.save(collaborationRequest);
	}
	
	@PutMapping("/addCollaborator/{idTeacher}")
	public void addCollaborator(@PathVariable("idTeacher") long id,
			@RequestBody SchoolProject project) {
		SchoolProject schoolProject = schoolProjectService.getOne(project.getId()).get();
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		
		Set <SchoolTeacher> collaborators = schoolProject.getCollaborators();
		collaborators.add(schoolTeacher);
		schoolProject.setCollaborators(collaborators);
		schoolProjectService.save(schoolProject);
		
		/*
		Set <SchoolProject> schoolProjects = schoolTeacher.getSchoolProjects();
		schoolProjects.add(schoolProject);
		schoolTeacher.setSchoolProjects(schoolProjects);
		*/
	}
	
	@PutMapping("/refuseCollaborationRequest/{id}")
	public void refuseCollaborationRequest(@PathVariable("id") long id,
			@RequestBody SchoolProject project) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationRequest.setCollaborationResponse(CollaborationResponse.REFUSED);
		collaborationsRequestService.save(collaborationRequest);
	}
	
	@DeleteMapping("/deleteCollaborationRequest/{id}")
	public void deleteCollaborationRequest(@PathVariable("id") long id) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationsRequestService.delete(collaborationRequest);
	}

}
