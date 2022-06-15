package com.app.togglev1.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.togglev1.dtos.StudiesDTO;
import com.app.togglev1.entities.StudiesCycle;
import com.app.togglev1.services.StudiesService;

@RestController
@RequestMapping("/studies")
@CrossOrigin
public class StudiesController {

	@Autowired
	StudiesService studiesService;
	
	@GetMapping("/list/dto")
	public ResponseEntity<List<StudiesDTO>> getAllDto() {
		List<StudiesCycle> list = studiesService.list();
		List<StudiesDTO> listStudiesDTO = new ArrayList<>();
		for(StudiesCycle sc : list) {
			
			StudiesDTO studiesDTO = new StudiesDTO(
						sc.getCode(), 
						sc.getName(), 
						sc.getD_studiesFamily().getName(), 
						sc.getC_studiesLevel().getName(),
						sc.getD_studiesFamily().getCode(),
						sc.getC_studiesLevel().getId()
					);
			listStudiesDTO.add(studiesDTO);
		}
		return new ResponseEntity<List<StudiesDTO>>(listStudiesDTO, HttpStatus.OK);
	}
	@GetMapping("/list")
	public ResponseEntity<List<StudiesCycle>> getAll() {
		List<StudiesCycle> list = studiesService.list();
		return new ResponseEntity<List<StudiesCycle>>(list, HttpStatus.OK);
	}
	@GetMapping("/one/{code}")
	public ResponseEntity<StudiesDTO> getOne(@PathVariable("code") String code) {
		StudiesCycle cycle = studiesService.getOne(code);
		StudiesDTO studiesDTO = new StudiesDTO();
		studiesDTO.setName(cycle.getName());
		studiesDTO.setCode(cycle.getCode());
		studiesDTO.setFamily(cycle.getD_studiesFamily().getName());
		studiesDTO.setLevel(cycle.getC_studiesLevel().getName());
		return new ResponseEntity<StudiesDTO>(studiesDTO, HttpStatus.OK);
	}
	
}
