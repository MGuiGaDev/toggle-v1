package com.app.togglev1.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.repositories.SchoolProjectRepository;

@Service
@Transactional
public class SchoolProjectService {

	@Autowired
	SchoolProjectRepository projectRepository;

	public void save(SchoolProject schoolProject) {
		projectRepository.save(schoolProject);
	}

	public Optional<SchoolProject> getOne(long id) {
		return projectRepository.findById(id);
	}

	public Optional<SchoolProject> getByTitle(String title) {
		return projectRepository.findByTitle(title);
	}

	public List<SchoolProject> getAll() {
		return projectRepository.findAll();
	}

	public Set<SchoolProject> getCollaborativeProject(SchoolTeacher teacher) {
		return projectRepository.getCollaborativeProjects(teacher);
	}

	public List<SchoolProject> getAllDifferent(long id) {
		return projectRepository.getAllDifferent(id);
	}

	public void delete(SchoolProject schoolProject) {
		projectRepository.delete(schoolProject);
	}

}
