package com.pedrodev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pedrodev.repositories.UsuarioRepository;

@Service
public class AuthorizationService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserDetails userDetails = usuarioRepository.findByEmail(username);
		
		if (userDetails == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		
		return userDetails;
	}
	
	

}
