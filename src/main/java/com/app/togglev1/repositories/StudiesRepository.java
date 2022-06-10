package com.app.togglev1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.entities.StudiesCycle;

@Repository
public interface StudiesRepository extends JpaRepository<StudiesCycle, String>{

	StudiesCycle getByCode(String code);
	
	
}
