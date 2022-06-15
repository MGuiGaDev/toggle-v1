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
@CrossOrigin
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
		SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
		schoolTeacherDTO.setSchoolProfileName(schoolProfile.getName());
		schoolTeacherDTO.setIdSchoolProfile(schoolProfile.getId());
		return new ResponseEntity<SchoolTeacherDTO>(schoolTeacherDTO, HttpStatus.OK);
	}

	@PutMapping("/updateTeacher/{id}")
	public ResponseEntity<Mensaje> updateTeacher(@PathVariable("id") long id, @RequestBody SchoolTeacherDTO newUser) {
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(newUser.getId()).get();
		BasicUser basicUser = basicUserService.getByUserName(schoolTeacher.getUserNested().getUsername()).get();
		basicUser.setEmail(newUser.getEmail());
		basicUser.setName(newUser.getName());
		basicUserService.save(basicUser); 
		return new ResponseEntity<Mensaje>(new Mensaje("Perfil actualizado"), HttpStatus.OK);
	}
	
	@PutMapping("/updateTeacherPassword/{id}")
	public ResponseEntity<Mensaje> updatePasswordTeacher(@PathVariable("id") long id, @RequestBody SchoolTeacherDTO newUser) {
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(newUser.getId()).get();
		BasicUser basicUser = basicUserService.getByUserName(schoolTeacher.getUserNested().getUsername()).get();
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

	@PutMapping("/updateInfoProject/{id}")
	public ResponseEntity<Mensaje> updateInfoProject(@PathVariable("id") long id,
			@RequestBody SchoolProjectDTO schoolProject) {
		SchoolProject project = schoolProjectService.getOne(id).get();
		project.setDescription(schoolProject.getDescription());
		project.setTitle(schoolProject.getTitle());
		schoolProjectService.save(project);
		return new ResponseEntity<Mensaje>(new Mensaje("Proyecto actualizado"), HttpStatus.OK);
	}

	@PutMapping("/addCycleToProyect/{id}")
	public ResponseEntity<Mensaje> addCycleToProyect(@PathVariable("id") long id, @RequestBody StudiesDTO studiesDTO) {
		SchoolProject project = schoolProjectService.getOne(id).get();
		StudiesCycle studiesCycle = studiesService.getOne(studiesDTO.getCode());
		Set<StudiesCycle> studiesCycles = project.getListStudiesCycle();
		studiesCycles.add(studiesCycle);
		project.setListStudiesCycle(studiesCycles);
		schoolProjectService.save(project);
		System.out.println(project.toString());
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
		Set<CollaborationRequest> collaborationRequests = collaborationsRequestService
				.getAllBySchoolProyect(schoolProject);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for (CollaborationRequest collaboration : collaborationRequests) {
			CollaborationRequestDTO c = new CollaborationRequestDTO();
			c.setId(collaboration.getId());
			c.setIdTeacher(collaboration.getSchoolTeacherRequest().getId());
			c.setNameTeacher(collaboration.getSchoolTeacherRequest().getUserNested().getName());
			SchoolTeacher schoolTeacher = schoolTeacherService.getOne(collaboration.getSchoolTeacherRequest().getId())
					.get();
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
		Set<StudiesCycle> studiesCycles = new HashSet<>();
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
		Set<StudiesCycle> studiesCycles = new HashSet<>();
		studiesCycles = schoolTeacher.getListStudiesCycle();
		Iterator<StudiesCycle> ite = studiesCycles.iterator();
		while (ite.hasNext()) {
			StudiesCycle studiesCycle = ite.next();
			if (studiesCycle.getCode().equals(cycle.getCode())) {
				ite.remove();
			}
		}
		schoolTeacher.setListStudiesCycle(studiesCycles);
		schoolTeacherService.save(schoolTeacher);
	}

	@DeleteMapping("/deleteProject/{id}")
	public void deleteProject(@PathVariable("id") long id) {
		SchoolProject schoolProject = schoolProjectService.getOne(id).get();
		schoolProject.setCollaborators(null);
		schoolProjectService.save(schoolProject);
		schoolProjectService.delete(schoolProject);
	}

	@PostMapping("/addCollaborationRequest/{username}")
	public void addCollaborationRequest(@PathVariable("username") String username,
			@RequestBody CollaborationRequest cRequest) {
		SchoolProject schoolProject = schoolProjectService.getOne(cRequest.getId()).get();
		SchoolTeacher schoolTeacher2 = schoolTeacherService.getByUserName(username).get();
		CollaborationRequest collaborationRequest = new CollaborationRequest();
		collaborationRequest.setMessage(cRequest.getMessage());
		collaborationRequest.setSchoolProject(schoolProject);
		collaborationRequest.setSchoolTeacherRequest(schoolTeacher2);
		collaborationRequest.setSended(new Date());
		collaborationRequest.setCollaborationResponse(CollaborationResponse.PENDINT);
		collaborationsRequestService.save(collaborationRequest);
	}

	@PutMapping("/aceptCollaborationRequest/{id}")
	public void aceptCollaborationRequest(@PathVariable("id") long id, @RequestBody SchoolProject project) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationRequest.setCollaborationResponse(CollaborationResponse.ACEPTED);
		collaborationsRequestService.save(collaborationRequest);
	}

	@PutMapping("/addCollaborator/{idTeacher}")
	public void addCollaborator(@PathVariable("idTeacher") long id, @RequestBody SchoolProject project) {
		SchoolProject schoolProject = schoolProjectService.getOne(project.getId()).get();
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		Set<SchoolTeacher> collaborators = schoolProject.getCollaborators();
		collaborators.add(schoolTeacher);
		schoolProject.setCollaborators(collaborators);
		schoolProjectService.save(schoolProject);
	}

	@PutMapping("/refuseCollaborationRequest/{id}")
	public void refuseCollaborationRequest(@PathVariable("id") long id, @RequestBody SchoolProject project) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationRequest.setCollaborationResponse(CollaborationResponse.REFUSED);
		collaborationsRequestService.save(collaborationRequest);
	}

	@GetMapping("/getAllMyCollaboratorRequest/{id}")
	public ResponseEntity<Set<CollaborationRequestDTO>> getMyCollaboratorRequest(@PathVariable("id") long id) {
		SchoolTeacher schoolTeacher = schoolTeacherService.getOne(id).get();
		Set<CollaborationRequest> collaborationRequest = collaborationsRequestService
				.getAllBySchoolTeacher(schoolTeacher);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for (CollaborationRequest request : collaborationRequest) {
			CollaborationRequestDTO dto = new CollaborationRequestDTO();
			dto.setTitleProject(request.getSchoolProject().getTitle());
			dto.setCollaborationResponse(request.getCollaborationResponse());
			dto.setNameSchool(request.getSchoolProject().getSchoolTeacherCreator().getSchoolProfile().getName());
			dto.setSend(request.getSended());
			collaborationRequestDTOs.add(dto);
		}
		return new ResponseEntity<Set<CollaborationRequestDTO>>(collaborationRequestDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/getMyCollaboratorRequestPendint/{id}")
	public ResponseEntity<Set<CollaborationRequestDTO>> getMyCollaboratorRequestPendint(@PathVariable("id") long id) {
		Set<CollaborationRequest> collaborationRequest = collaborationsRequestService.getMyCollaboratorRequestPendint(id);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for (CollaborationRequest request : collaborationRequest) {
			CollaborationRequestDTO dto = new CollaborationRequestDTO();
			dto.setTitleProject(request.getSchoolProject().getTitle());
			dto.setCollaborationResponse(request.getCollaborationResponse());
			dto.setNameSchool(request.getSchoolProject().getSchoolTeacherCreator().getSchoolProfile().getName());
			dto.setSend(request.getSended());
			collaborationRequestDTOs.add(dto);
		}
		return new ResponseEntity<Set<CollaborationRequestDTO>>(collaborationRequestDTOs, HttpStatus.OK);
	}

	@GetMapping("/getAllCollaborationRequest/{id}")
	public ResponseEntity<Set<CollaborationRequestDTO>> getAllCollaborationRequest(@PathVariable("id") long id) {
		SchoolProject project = schoolProjectService.getOne(id).get();
		Set<CollaborationRequest> collaborationRequest = collaborationsRequestService.getAllBySchoolProyect(project);
		Set<CollaborationRequestDTO> collaborationRequestDTOs = new HashSet<>();
		for (CollaborationRequest cR : collaborationRequest) {
			CollaborationRequestDTO dto = new CollaborationRequestDTO();
			dto.setCitySchool(cR.getSchoolProject().getSchoolTeacherCreator().getSchoolProfile().getCity());
			dto.setNameSchool(cR.getSchoolProject().getSchoolTeacherCreator().getSchoolProfile().getName());
			dto.setNameTeacher(cR.getSchoolTeacherRequest().getUserNested().getUsername());
			dto.setSend(cR.getSended());
			dto.setMessage(cR.getMessage());
			collaborationRequestDTOs.add(dto);
		}
		return new ResponseEntity<Set<CollaborationRequestDTO>>(collaborationRequestDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/getProjectsNoCollaboratorRequest/{id}")
	public ResponseEntity<Set<SchoolProject>> getProjectsNoCollaborator(@PathVariable("id") long id) {
		SchoolTeacher teacher = schoolTeacherService.getOne(id).get();
		Set<SchoolProject> projects = collaborationsRequestService.getProjectsNoCollaboratorRequest(teacher); 
		System.out.println(projects.toString());
		return new ResponseEntity<Set<SchoolProject>>(projects, HttpStatus.OK);
	}

	@DeleteMapping("/deleteCollaborationRequest/{id}")
	public void deleteCollaborationRequest(@PathVariable("id") long id) {
		CollaborationRequest collaborationRequest = collaborationsRequestService.findById(id);
		collaborationsRequestService.delete(collaborationRequest);
	}

}
