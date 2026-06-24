package com.pedrodev.exceptions;

@SuppressWarnings("serial")
public class ErroNegocioException extends RuntimeException {
	
	public ErroNegocioException(String message) {
		super(message);
	}

}
