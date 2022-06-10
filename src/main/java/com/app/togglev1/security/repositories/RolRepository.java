package com.app.togglev1.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.security.entities.Rol;
import com.app.togglev1.security.enums.RolName;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

	Optional <Rol> findByRolName(RolName rolName);
	boolean existsByRolName(RolName rolName);
	
}
