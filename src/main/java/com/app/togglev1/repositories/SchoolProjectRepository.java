package com.app.togglev1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.togglev1.entities.SchoolProject;

public interface SchoolProjectRepository extends JpaRepository<SchoolProject, Long>{

	Optional<SchoolProject> findByTitle(String title);
	@Query("SELECT sp FROM SchoolProject sp WHERE sp.schoolTeacherCreator.id != :idTeacher")
	List<SchoolProject> getAllDifferent(@Param("idTeacher") long idTeacher);
	@Query(
			value = "SELECT * FROM school_project_collaborators WHERE school_project_collaborators.school_teacher_id = :idTeacher",
			nativeQuery = true
			)
	List<SchoolProject>getCollaborativeProjects(long idTeacher);
	
}
