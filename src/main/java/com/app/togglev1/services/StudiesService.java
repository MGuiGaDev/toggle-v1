package com.app.togglev1.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.repositories.StudiesRepository;

@Service
@Transactional
public class StudiesService {

	@Autowired
	StudiesRepository studiesRepository;
	
	public List<StudiesCycle> list() {
		return studiesRepository.findAll();
	}
	
	public StudiesCycle getOne(String code) {
		return studiesRepository.getByCode(code);
	}
	
}
