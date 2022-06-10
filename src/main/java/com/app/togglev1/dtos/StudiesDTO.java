package com.app.togglev1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudiesDTO {
	
	private String code;
	private String name;
	private String family;
	private String level;
	private String idFamily;
	private int idLevel;

}
