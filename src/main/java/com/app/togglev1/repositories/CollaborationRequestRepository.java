package com.app.togglev1.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;

@Repository
public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long>{
	
	Set <CollaborationRequest> findAllBySchoolProject(SchoolProject project);
	Set <CollaborationRequest> findAllBySchoolTeacherRequest(SchoolTeacher schoolTeacher);
	
	@Query("SELECT cr FROM CollaborationRequest cr WHERE cr.schoolTeacherRequest.id = :idTeacher AND cr.collaborationResponse='PENDINT' ")
	Set <CollaborationRequest> getMyCollaboratorRequestPendint(@Param("idTeacher") long idTeacher);
	
	@Query("SELECT cr FROM CollaborationRequest cr WHERE cr.schoolProject.id = :idProject AND cr.collaborationResponse = 'PENDINT' ")
	Set <CollaborationRequest> getCollaboratorRequestPendintOfProject(@Param("idProject") long idProject);
	
	@Query("SELECT DISTINCT sp FROM SchoolProject sp, CollaborationRequest cr WHERE cr.schoolProject.id IS NULL OR cr.schoolTeacherRequest.id <> :idTeacher AND sp.schoolTeacherCreator.id <> :idTeacher")
	Set<SchoolProject> getProjectsNoCollaboratorRequest(@Param("idTeacher") long idTeacher);
	
	boolean existsById(Long id);
}
