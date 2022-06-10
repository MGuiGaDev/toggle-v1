package com.app.togglev1.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.togglev1.entities.SchoolProject;

public interface SchoolProjectRepository extends JpaRepository<SchoolProject, Long>{

	Optional<SchoolProject> findByTitle(String title);
	
}
