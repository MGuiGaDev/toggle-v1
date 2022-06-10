package com.app.togglev1.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class StudiesCycle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String code;
	
	@NonNull
	@Column(unique=true)
	private String name;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="studiesLevel", referencedColumnName = "id", nullable = false)
	private StudiesLevel c_studiesLevel;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="studiesFamilyCode", referencedColumnName = "code", nullable = false)
	private StudiesFamily d_studiesFamily;

	public StudiesCycle(String code, @NonNull String name, @NonNull StudiesLevel c_studiesLevel,
			@NonNull StudiesFamily d_studiesFamily) {
		this.code = code;
		this.name = name;
		this.c_studiesLevel = c_studiesLevel;
		this.d_studiesFamily = d_studiesFamily;
	}
	
	

}
