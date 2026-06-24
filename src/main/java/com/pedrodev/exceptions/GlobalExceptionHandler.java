package com.pedrodev.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsuarioNaoEncontrado(UsernameNotFoundException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(PlacaInvalidaException.class)
	public ResponseEntity<String> handlePlacaInvalida(PlacaInvalidaException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(EstacionamentoExistenteException.class)
	public ResponseEntity<String> handleEstacionamentoExistente(EstacionamentoExistenteException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

}
