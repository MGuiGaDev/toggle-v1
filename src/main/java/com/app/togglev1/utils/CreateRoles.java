package com.app.togglev1.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.togglev1.security.entities.Rol;
import com.app.togglev1.security.enums.RolName;
import com.app.togglev1.security.services.RolService;

@Component
public class CreateRoles implements CommandLineRunner{

	@Autowired
	RolService rolService;
	@Override
	public void run(String... args) throws Exception {
		
		 //En modo create esto debe estar descomentado,
		 // en modo update, no es necesario que se realice de nuevo esta acción
		/*
		Rol rolAdmin = new Rol(RolName.ROLE_ADMIN);
		Rol rolUser = new Rol(RolName.ROLE_MANAGER_CENTER);
		Rol rolTeacher = new Rol(RolName.ROLE_TEACHER_CENTER);
		rolService.save(rolUser);
		rolService.save(rolAdmin);
		rolService.save(rolTeacher);
		*/
		
	}

}
