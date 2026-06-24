package com.pedrodev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.pedrodev.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	public UserDetails findByEmail(String email);
	
	

}
