package com.app.togglev1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.entities.SchoolProfile;

@Repository
public interface SchoolProfileRepository extends JpaRepository<SchoolProfile, Long>{

	
	boolean existsByName(String name);
	boolean existsById(Long id);
	
	
}
