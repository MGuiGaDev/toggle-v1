package com.app.togglev1.services;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.repositories.CollaborationRequestRepository;

@Service
@Transactional
public class CollaborationsRequestService {
	
	@Autowired
	CollaborationRequestRepository collaborationRequestRepository;
	
	public void save(CollaborationRequest collaborationRequest) {
		collaborationRequestRepository.save(collaborationRequest);
	}
	
	public void delete(CollaborationRequest collaborationRequest) {
		collaborationRequestRepository.delete(collaborationRequest);
	}
	
	public Set<CollaborationRequest> getAllBySchoolProyect(SchoolProject schoolProject) {
		Set<CollaborationRequest> collaborationRequests = collaborationRequestRepository.findAllBySchoolProject(schoolProject);
		return collaborationRequests;
	}
	
	public CollaborationRequest findById(long id) {
		return collaborationRequestRepository.getById(id);
	}
	

}
