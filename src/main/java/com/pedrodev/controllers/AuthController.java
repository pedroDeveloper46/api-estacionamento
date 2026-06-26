package com.pedrodev.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedrodev.exceptions.ErrorResponse;
import com.pedrodev.models.LoginDTO;
import com.pedrodev.models.LoginResponseDTO;
import com.pedrodev.models.Usuario;
import com.pedrodev.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/autenticacao")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping(path="/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) {
		
			
		try {
	        var usernamePassword = new UsernamePasswordAuthenticationToken(
	            login.getEmail(), login.getSenha()
	        );

	        var auth = authenticationManager.authenticate(usernamePassword);

	        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

	        return ResponseEntity.ok(new LoginResponseDTO(token));

	    } catch (AuthenticationException e) {
	    	
	    	ErrorResponse errorResponse = new ErrorResponse(
	    		HttpStatus.BAD_REQUEST.value(),
	    		"Erro de negócio",
	    		"Credenciais inválidas: Verifique seu e-mail e senha",
	    		LocalDateTime.now()
	    	
	    	);
	    	
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }
	}

}
