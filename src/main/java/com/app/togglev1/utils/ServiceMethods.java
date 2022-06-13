package com.app.togglev1.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.app.togglev1.dtos.CollaboratorProjectDTO;
import com.app.togglev1.entities.CollaborationRequest;
import com.app.togglev1.entities.SchoolProject;
import com.app.togglev1.entities.SchoolTeacher;
import com.app.togglev1.entities.StudiesCycle;

public interface ServiceMethods {

	static Set<String> extractCyclesNames(Set<StudiesCycle> cycles) {
		Set<String> nameCycles = new HashSet<String>();
		if (!cycles.isEmpty()) {
			for (StudiesCycle c : cycles) {
				String nameCycle = c.getName();
				nameCycles.add(nameCycle);
			}
		}
		return nameCycles;
	}

	static Set<CollaboratorProjectDTO> extractCollaborators(SchoolProject project) {
		Set<SchoolTeacher> schoolTeachers = project.getCollaborators();
		Set<CollaboratorProjectDTO> collaborators = new HashSet<>();
		for (SchoolTeacher st : schoolTeachers) {
			CollaboratorProjectDTO collaboratorProjectDTO = new CollaboratorProjectDTO();
			collaboratorProjectDTO.setNameCollaborator(st.getUserNested().getUsername());
			collaborators.add(collaboratorProjectDTO);
		}
		return collaborators;
	}

	static Boolean checkIfExistsCollaborator(Set<CollaboratorProjectDTO> collaboratorProjectDTOs,
			SchoolTeacher schoolTeacher) {
		boolean exists = false;
		if (!collaboratorProjectDTOs.isEmpty()) {
			for (CollaboratorProjectDTO st : collaboratorProjectDTOs) {
				if (st.getNameCollaborator().equals(schoolTeacher.getUserNested().getName())) {
					exists = true;
				}
			}
		}
		return exists;
	}

	static Set<SchoolProject> filterCollaborationRequestByTeacher(List<SchoolProject> sp, SchoolTeacher st) {
		Set<SchoolProject> filterProjects = new HashSet<>();
		if (!sp.isEmpty()) {
			for (SchoolProject sP : sp) {
				if (!sP.getCollaborationRequests().isEmpty()) {
					for (CollaborationRequest cR : sP.getCollaborationRequests()) {
						System.out.println(cR.toString());
						if (cR.getSchoolTeacherRequest().getId() != st.getId()) {
							filterProjects.add(sP);
						}
					}

				} else {
					filterProjects.add(sP);
				}
			}
		}
		
		return filterProjects;
	}

	static Boolean checkIfExists(SchoolProject project, SchoolTeacher schoolTeacher) {
		boolean exists = false;
		Set<SchoolTeacher> schoolTeachers = project.getCollaborators();
		for (SchoolTeacher st : schoolTeachers) {
			if (st.getId() == schoolTeacher.getId()) {
				exists = true;
			}
		}
		return exists;
	}

}
