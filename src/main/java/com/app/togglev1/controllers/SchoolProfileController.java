package com.app.togglev1.controllers;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.services.SchoolProfileService;
import com.app.togglev1.services.SchoolTeacherService;
import com.app.togglev1.services.StudiesService;
import com.app.togglev1.dtos.Mensaje;
import com.app.togglev1.dtos.SchoolProfileDTO;
import com.app.togglev1.dtos.StudiesDTO;
import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.security.dtos.NewUser;
import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.entities.UserManager;
import com.app.togglev1.security.services.BasicUserService;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class SchoolProfileController {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SchoolProfileService schoolProfileService;
	
	@Autowired
	BasicUserService basicUserService;
	
	@Autowired
	SchoolTeacherService schoolTeacherService;
	
	@Autowired
	StudiesService studiesService;
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") long id){
		if(!schoolProfileService.existsById(id)) 
			return new ResponseEntity<Mensaje>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
		SchoolProfile schoolProfile = schoolProfileService.getOne(id).get();
		Set <SchoolTeacher> schoolTeachers = schoolTeacherService.getAllTeacherOfSchool(schoolProfile);
		SchoolProfileDTO schoolProfileDTO = new SchoolProfileDTO();
		schoolProfileDTO.setId(id);
		schoolProfileDTO.setName(schoolProfile.getName());
		schoolProfileDTO.setUserManager(schoolProfile.getUserManager());
		schoolProfileDTO.setListTeachers(schoolTeachers);
		return new ResponseEntity<SchoolProfileDTO>(schoolProfileDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/profile/{username}")
	public ResponseEntity<?> getByUsername(@PathVariable("username") String username){
		UserManager basicUser = new UserManager();
		basicUser = (UserManager) basicUserService.getByUserName(username).get();
		SchoolProfile schoolProfile = schoolProfileService.getOne(basicUser.getSchoolProfile().getId()).get();
		Set <SchoolTeacher> schoolTeachers = schoolTeacherService.getAllTeacherOfSchool(schoolProfile);
		SchoolProfileDTO schoolProfileDTO = new SchoolProfileDTO();
		schoolProfileDTO.setId(schoolProfile.getId());
		schoolProfileDTO.setName(schoolProfile.getName());
		schoolProfileDTO.setUserManager(schoolProfile.getUserManager());
		schoolProfileDTO.setListTeachers(schoolTeachers);
		schoolProfileDTO.setListStudiesCycle(schoolProfile.getListStudiesCycle());
		schoolProfileDTO.setCity(schoolProfile.getCity());
		schoolProfileDTO.setDescription(schoolProfile.getDescription());
		return new ResponseEntity<SchoolProfileDTO>(schoolProfileDTO, HttpStatus.OK);
	}
	
	//https://www.baeldung.com/spring-security-method-security
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER_CENTER') or hasRole('ROLE_TEACHER_CENTER')")
	@GetMapping("/list")
	public ResponseEntity<List<SchoolProfile>> list() {
		List <SchoolProfile> list = schoolProfileService.list();
		return new ResponseEntity<List<SchoolProfile>>(list, HttpStatus.OK);
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Mensaje> update(@PathVariable("id") int id, @RequestBody SchoolProfile schoolProfile) {
		if(!schoolProfileService.existsById(id))
			return new ResponseEntity<Mensaje>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
		
		if(StringUtils.isBlank(schoolProfile.getName()))
			return new ResponseEntity<Mensaje>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

		
		
		SchoolProfile updateSP = schoolProfileService.getOne(id).get();
		updateSP.setName(schoolProfile.getName());
		updateSP.setCity(schoolProfile.getCity());
		updateSP.setDescription(schoolProfile.getDescription());
		schoolProfileService.save(updateSP);
		return new ResponseEntity<Mensaje>(new Mensaje("Producto actualizado"), HttpStatus.OK);
	}
	
	@PutMapping("/updateCycle/{code}")
	public ResponseEntity<Mensaje> updateCycle(@PathVariable("code") String code, @RequestBody SchoolProfile schoolProfile) {
		if(!schoolProfileService.existsById(schoolProfile.getId()))
			return new ResponseEntity<Mensaje>(new Mensaje("No existe"), HttpStatus.NOT_FOUND);
		if(StringUtils.isBlank(schoolProfile.getName()))
			return new ResponseEntity<Mensaje>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

		SchoolProfile updateSP = schoolProfileService.getOne(schoolProfile.getId()).get();
		StudiesCycle cycle = new StudiesCycle();
		Set <StudiesCycle> studiesCycles = new HashSet<>();
		studiesCycles = updateSP.getListStudiesCycle();
		cycle = studiesService.getOne(code);
		studiesCycles.add(cycle);
		updateSP.setListStudiesCycle(studiesCycles);
		schoolProfileService.save(updateSP);
		return new ResponseEntity<Mensaje>(new Mensaje("Ciclo añadido"), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteTeacher/{username}")
	public void deleteTeacher(@PathVariable("username") String username) {
		BasicUser basicUser = basicUserService.getByUserName(username).get();
		basicUserService.delete(basicUser);
	}
	@DeleteMapping("/deleteCycle/{id}")
	public void deleteCycle(@PathVariable("id") long id, @RequestBody StudiesDTO cycle) {
		
		SchoolProfile schoolProfileUpdate = new SchoolProfile();
		schoolProfileUpdate = schoolProfileService.getOne(id).get();
		Set <StudiesCycle> studiesCycles = new HashSet<>();
		studiesCycles = schoolProfileUpdate.getListStudiesCycle();
		Iterator <StudiesCycle> ite = studiesCycles.iterator();
		while(ite.hasNext()) {
			StudiesCycle studiesCycle = ite.next();
			if(studiesCycle.getCode().equals(cycle.getCode())){
				ite.remove();
			}
		}
		schoolProfileUpdate.setListStudiesCycle(studiesCycles);
		schoolProfileService.save(schoolProfileUpdate);
	}
	
	@PutMapping("/updateManager/{id}")
	public ResponseEntity<?> updateManager(@PathVariable("id") int id, @RequestBody NewUser basicUser){
		BasicUser user = basicUserService.getOneBasic(id).get();
		user.setEmail(basicUser.getEmail());
		user.setName(basicUser.getName());
		user.setUsername(basicUser.getUsername());
		user.setPassword(passwordEncoder.encode(basicUser.getPassword()));
		basicUserService.save(user);
		return new ResponseEntity<BasicUser>(user, HttpStatus.OK);
	}
	
	
	
}
