package com.app.togglev1.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.togglev1.security.entities.BasicUser;
@Repository
public interface BasicUserRepository extends JpaRepository<BasicUser, Long>{
	Optional<BasicUser> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
}
