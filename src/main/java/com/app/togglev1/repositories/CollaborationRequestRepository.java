package com.app.togglev1.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProject;

@Repository
public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long>{
	Set <CollaborationRequest> findAllBySchoolProject(SchoolProject project);
}
