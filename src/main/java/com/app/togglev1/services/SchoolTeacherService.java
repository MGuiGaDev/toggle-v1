package com.app.togglev1.services;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.repositories.SchoolTeacherRepository;

@Service
@Transactional
public class SchoolTeacherService {
	
	@Autowired
	SchoolTeacherRepository schoolTeacherRepository;
	
	public Set<SchoolTeacher> getAllTeacherOfSchool(SchoolProfile schoolProfile){
		return schoolTeacherRepository.findAllBySchoolProfile(schoolProfile);
	}
	
	public Optional<SchoolTeacher> getByUserNestedId(Long idUserNested){
		return schoolTeacherRepository.findByUserNestedId(idUserNested);
	}
	
	public Optional<SchoolTeacher> getOne(long id)  {
		return schoolTeacherRepository.findById(id);
	}
	
	
	public boolean existsById(long id) {
		return schoolTeacherRepository.existsById(id);
	}
	
	public void save(SchoolTeacher schoolTeacher) {
		schoolTeacherRepository.save(schoolTeacher);
	}
	
	public Optional<SchoolTeacher> getByUserName(String username) {
		return schoolTeacherRepository.findByUserNestedUsername(username);
	}

}
