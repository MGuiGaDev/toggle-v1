package com.app.togglev1.security.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.dtos.Mensaje;
import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.security.dtos.JwtDTO;
import com.app.togglev1.security.dtos.LoginUser;
import com.app.togglev1.security.dtos.NewUser;
import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.entities.Rol;
import com.app.togglev1.security.entities.UserManager;
import com.app.togglev1.security.entities.UserNested;
import com.app.togglev1.security.enums.RolName;
import com.app.togglev1.security.jwt.JwtProvider;
import com.app.togglev1.security.services.BasicUserService;
import com.app.togglev1.security.services.RolService;
import com.app.togglev1.services.SchoolProfileService;
import com.app.togglev1.services.StudiesService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	BasicUserService basicUserService;
	@Autowired
	RolService rolService;
	@Autowired
	SchoolProfileService schoolProfileService;
	@Autowired
	JwtProvider jwtProvider;
	@Autowired
	StudiesService studiesService;

	@PostMapping("/create-acount-teacher")
	public ResponseEntity<?> nuevoProfesor(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {

		UserNested basicUser = new UserNested(newUser.getName(), newUser.getUsername(), newUser.getEmail(),
				passwordEncoder.encode(newUser.getPassword()));
		SchoolProfile schoolProfile = schoolProfileService.getOne(newUser.getIdSchoolProfile()).get();
		Set<Rol> roles = new HashSet<>();
		roles.add(rolService.getByRolName(RolName.ROLE_TEACHER_CENTER).get());
		basicUser.setRoles(roles);
		SchoolTeacher st = new SchoolTeacher();
		st.setUserNested(basicUser);
		Set<SchoolTeacher> schoolTeachers = schoolProfile.getSchoolTeachers();
		schoolTeachers.add(st);
		schoolProfile.setSchoolTeachers(schoolTeachers);
		st.setSchoolProfile(schoolProfile);
		basicUser.setSchoolTeacher(st);
		basicUserService.save(basicUser);
		return new ResponseEntity<Mensaje>(new Mensaje("usuario guardado"), HttpStatus.CREATED);
	}

	@PostMapping("/create-account")
	public ResponseEntity<?> nuevo(@Valid @RequestBody NewUser newUser) {
		if (basicUserService.existsByUserName(newUser.getUsername())) {
			return new ResponseEntity<Mensaje>(new Mensaje("El nombre ya existe"), HttpStatus.BAD_REQUEST);
		}
		if (basicUserService.existsByEmail(newUser.getEmail())) {
			return new ResponseEntity<Mensaje>(new Mensaje("El mail ya existe"), HttpStatus.BAD_REQUEST);
		}

		if (schoolProfileService.existsByName(newUser.getSchoolProfile().getName())) {
			return new ResponseEntity<Mensaje>(new Mensaje("El nombre de centro ya existe"), HttpStatus.BAD_REQUEST);
		}

		UserManager basicUser = new UserManager(newUser.getName(), newUser.getUsername(), newUser.getEmail(),
				passwordEncoder.encode(newUser.getPassword()));

		SchoolProfile schoolProfile = new SchoolProfile();
		schoolProfile.setName(newUser.getSchoolProfile().getName());
		schoolProfile.setCode(newUser.getSchoolProfile().getCode());
		Set<StudiesCycle> cycles = new HashSet<>();
		for (StudiesCycle c : newUser.getSchoolProfile().getListStudiesCycle()) {
			StudiesCycle sC = studiesService.getOne(c.getCode());
			cycles.add(sC);
		}
		schoolProfile.setListStudiesCycle(cycles);
		basicUser.setSchoolProfile(schoolProfile);
		schoolProfile.setUserManager(basicUser);

		Set<Rol> roles = new HashSet<>();
		roles.add(rolService.getByRolName(RolName.ROLE_MANAGER_CENTER).get());
		if (newUser.getRoles().contains("admin"))
			roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
		basicUser.setRoles(roles);
		basicUserService.save(basicUser);
		return new ResponseEntity<Mensaje>(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginUser loginUser){
		if (!basicUserService.existsByUserName(loginUser.getUsername())) {
			return new ResponseEntity<Mensaje>(new Mensaje("El nombre de usuario no existe."), HttpStatus.BAD_REQUEST);
		}else {
			try {
				Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
				String jwt = jwtProvider.generateToken(authentication);
				JwtDTO jwtDTO = new JwtDTO(jwt);
				BasicUser basicUser = basicUserService.getByUserName(loginUser.getUsername()).get();
				basicUser.setLastAccess(new Date());
				basicUserService.save(basicUser);
				return new ResponseEntity<JwtDTO>(jwtDTO, HttpStatus.OK);
			} catch(Exception  ex) {
				return new ResponseEntity<Mensaje>(new Mensaje("La contraseña no es correcta."), HttpStatus.BAD_REQUEST);
			}
		}

	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtDTO> refresh(@RequestBody JwtDTO jwtDTO) throws ParseException {
		String token = jwtProvider.refreshToken(jwtDTO);
		JwtDTO newJwtDTO = new JwtDTO(token);
		return new ResponseEntity<>(newJwtDTO, HttpStatus.OK);
	}

}
