package com.app.togglev1.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;

public interface SchoolProjectRepository extends JpaRepository<SchoolProject, Long>{

	Optional<SchoolProject> findByTitle(String title);
	@Query("SELECT sp FROM SchoolProject sp WHERE sp.schoolTeacherCreator.id != :idTeacher")
	List<SchoolProject> getAllDifferent(@Param("idTeacher") long idTeacher);
	
	@Query(value = "SELECT sp FROM SchoolProject sp WHERE :teacher MEMBER OF sp.collaborators")
	Set<SchoolProject>getCollaborativeProjects(@Param("teacher") SchoolTeacher teacher);
	
}
