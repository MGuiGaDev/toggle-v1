package com.app.togglev1.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.security.entities.Rol;
import com.app.togglev1.security.enums.RolName;
import com.app.togglev1.security.repositories.RolRepository;

@Service
@Transactional
public class RolService {
	
	@Autowired
	RolRepository rolRepository;
	
	public Optional<Rol> getByRolName(RolName rolName) {
		return rolRepository.findByRolName(rolName);
	}
	
	public void save(Rol rol) {
		rolRepository.save(rol);
	}
	

}
