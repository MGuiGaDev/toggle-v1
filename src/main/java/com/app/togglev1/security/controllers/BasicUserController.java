package com.app.togglev1.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.dtos.Mensaje;
import com.app.togglev1.dtos.SchoolTeacherDTO;
import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.services.BasicUserService;
import com.app.togglev1.services.SchoolProfileService;
import com.app.togglev1.services.SchoolTeacherService;

@RestController
@RequestMapping("/basicuser")
@CrossOrigin
public class BasicUserController {

	@Autowired
	BasicUserService basicUserService;
	
	@Autowired
	SchoolTeacherService schoolTeacherService;
	
	@Autowired
	SchoolProfileService schoolProfileService;
	
	@GetMapping("/detail/{username}")
	public ResponseEntity<?> getByUserName(@PathVariable("username") String username){
		if(!basicUserService.existsByUserName(username))
			return new ResponseEntity<Mensaje>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
		BasicUser basicUser = basicUserService.getByUserName(username).get();
		SchoolTeacher schoolTeacher = schoolTeacherService.getByUserNestedId(basicUser.getId()).get();
		SchoolTeacherDTO schoolTeacherDTO = new SchoolTeacherDTO();
		schoolTeacherDTO.setName(schoolTeacher.getUserNested().getName());
		schoolTeacherDTO.setEmail(schoolTeacher.getUserNested().getEmail());
		schoolTeacherDTO.setListStudiesCycle(schoolTeacher.getListStudiesCycle());
		schoolTeacherDTO.setSchoolProjects(schoolTeacher.getSchoolProyect());
		SchoolProfile schoolProfile = schoolProfileService.getOne(schoolTeacher.getSchoolProfile().getId()).get();
		schoolTeacherDTO.setSchoolProfileName(schoolProfile.getName());
		return new ResponseEntity<SchoolTeacherDTO>(schoolTeacherDTO, HttpStatus.OK);	
	}
	
	
	
	
}
