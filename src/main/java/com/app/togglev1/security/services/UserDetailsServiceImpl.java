package com.app.togglev1.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.togglev1.security.entities.BasicUser;
import com.app.togglev1.security.entities.MainUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	BasicUserService basicUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BasicUser basicUser = basicUserService.getByUserName(username).get();
		return MainUser.build(basicUser);
	}

}
