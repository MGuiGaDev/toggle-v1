package com.app.togglev1.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.repositories.BasicUserRepository;

@Service
@Transactional
public class BasicUserService {

	@Autowired
	BasicUserRepository basicUserRepository;
	
	public Optional<BasicUser>getOneBasic(long id) {
		return basicUserRepository.findById(id);
	}
	
	public Optional<BasicUser> getByUserName(String username){
		return basicUserRepository.findByUsername(username);
	}
	
	public boolean existsByUserName(String username) {
		return basicUserRepository.existsByUsername(username);
	}
	
	public boolean existsByEmail(String email) {
		return basicUserRepository.existsByEmail(email);
	}
	
	public void save(BasicUser basicUser) {
		basicUserRepository.save(basicUser);
	}
	
	public void delete(BasicUser basicUser) {
		basicUserRepository.delete(basicUser);
	}
	
}
