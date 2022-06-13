package com.app.togglev1.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.repositories.SchoolProfileRepository;

@Service
@Transactional
public class SchoolProfileService {
	
	@Autowired
	SchoolProfileRepository schoolProfileRepository;
	
	public List<SchoolProfile> list() {
		return schoolProfileRepository.findAll();
	}
	
	public boolean existsByName(String name) {
		return schoolProfileRepository.existsByName(name);
	}
	
	public boolean existsById(long id) {
		return schoolProfileRepository.existsById(id);
	}
	
	public Optional<SchoolProfile> getOne(long id)  {
		return schoolProfileRepository.findById(id);
	}
	
	public void save(SchoolProfile schoolProfile) {
		schoolProfileRepository.save(schoolProfile);
	}
	
	
	
	

}
