package com.pedrodev.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	
	@ExceptionHandler(ErroNegocioException.class)
	public ResponseEntity<ErrorResponse> handleErroNegocio(ErroNegocioException ex){
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Erro de négocio",
			ex.getMessage(),
			LocalDateTime.now()
		);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericNegocio(Exception ex){
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Erro interno",
			ex.getMessage(),
			LocalDateTime.now()
		);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValdationException(MethodArgumentNotValidException e){
		
		String messagem = e.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getDefaultMessage())
				.findFirst()
				.orElse("Erro de validação");
		
		ErrorResponse errorResponse = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Erro de validção",
				messagem,
				LocalDateTime.now()
		);
			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

}
