package com.pedrodev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedrodev.models.Usuario;
import com.pedrodev.models.UsuarioDTO;
import com.pedrodev.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping(path="/listar")
	public List<UsuarioDTO> listarUsuarios(){
		return usuarioService.listarUsuario();
	}
	
	@PostMapping(path="/cadastrar")
	public ResponseEntity<?> cadastrar(@RequestBody @Valid Usuario usuario){
		
		return ResponseEntity.ok(usuarioService.cadastrar(usuario));
		
	}
	
}
