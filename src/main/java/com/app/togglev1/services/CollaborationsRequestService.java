package com.app.togglev1.services;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.repositories.CollaborationRequestRepository;

@Service
@Transactional
public class CollaborationsRequestService {
	
	@Autowired
	CollaborationRequestRepository collaborationRequestRepository;
	
	public void save(CollaborationRequest collaborationRequest) {
		collaborationRequestRepository.save(collaborationRequest);
	}
	
	public boolean existsById(long id) {
		return collaborationRequestRepository.existsById(id);
	}
	
	public void delete(CollaborationRequest collaborationRequest) {
		collaborationRequestRepository.delete(collaborationRequest);
	}
	
	public Set<CollaborationRequest> getAllBySchoolProyect(SchoolProject schoolProject) {
		Set<CollaborationRequest> collaborationRequests = collaborationRequestRepository.findAllBySchoolProject(schoolProject);
		return collaborationRequests;
	}
	
	public Set<CollaborationRequest> getMyCollaboratorRequestPendint(long id){
		Set<CollaborationRequest> collaborationRequests = collaborationRequestRepository.getMyCollaboratorRequestPendint(id);
		return collaborationRequests;
	}
	
	public Set<CollaborationRequest> getCollaboratorRequestPendintOfProject(long id){
		Set<CollaborationRequest> collaborationRequests = collaborationRequestRepository.getCollaboratorRequestPendintOfProject(id);
		return collaborationRequests;
	}
	
	public Set<CollaborationRequest> getAllBySchoolTeacher(SchoolTeacher schoolTeacher) {
		Set<CollaborationRequest> collaborationRequests = collaborationRequestRepository.findAllBySchoolTeacherRequest(schoolTeacher);
		return collaborationRequests;
	}
	
	public Set<SchoolProject> getProjectsNoCollaboratorRequest(SchoolTeacher schoolTeacher) {
		Set<SchoolProject> schoolProjects = collaborationRequestRepository.getProjectsNoCollaboratorRequest(schoolTeacher.getId());
		return schoolProjects;
	}
	
	public CollaborationRequest findById(long id) {
		return collaborationRequestRepository.getById(id);
	}
	

}
