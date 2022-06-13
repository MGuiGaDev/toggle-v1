package com.app.togglev1.repositories;


import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.entities.SchoolProfile;
import com.app.togglev1.entities.SchoolTeacher;

@Repository
public interface SchoolTeacherRepository extends JpaRepository<SchoolTeacher, Long>{
	
	Set<SchoolTeacher> findAllBySchoolProfile(SchoolProfile schoolProfile);
	Optional<SchoolTeacher> findByUserNestedId(Long idUserNested);
	Optional<SchoolTeacher> findByUserNestedUsername(String username);
	
	
}
