package com.pedrodev.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedrodev.exceptions.ErroNegocioException;

import com.pedrodev.models.Usuario;
import com.pedrodev.models.UsuarioDTO;
import com.pedrodev.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Usuario cadastrar(Usuario usuario) {
		
		UserDetails usu = usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usu != null) {
			throw new ErroNegocioException("Usuário já existente");
		}
		
		String senhaCript = passwordEncoder.encode(usuario.getSenha());
		
		usuario.setSenha(senhaCript);
		  
		return usuarioRepository.save(usuario);
		
	}
	
	public List<UsuarioDTO> listarUsuario(){
		
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		List<UsuarioDTO> usuariosListaFinal = new ArrayList<>();
		
		for (Usuario usuario : usuarios) {
			
			UsuarioDTO usuarioDto = new UsuarioDTO();
			usuarioDto.setNome(usuario.getNome());
			usuarioDto.setEmail(usuario.getEmail());
			usuarioDto.setRole(usuario.getRole());
			
			usuariosListaFinal.add(usuarioDto);
		}
		
		return usuariosListaFinal;
		
	}
	
}
