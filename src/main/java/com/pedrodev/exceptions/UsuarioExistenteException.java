package com.pedrodev.exceptions;

@SuppressWarnings("serial")
public class UsuarioExistenteException extends RuntimeException {

	public UsuarioExistenteException(String message) {
		super(message);
	}
}
