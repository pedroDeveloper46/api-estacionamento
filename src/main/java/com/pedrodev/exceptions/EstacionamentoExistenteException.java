package com.pedrodev.exceptions;

@SuppressWarnings("serial")
public class EstacionamentoExistenteException extends RuntimeException {
	
	public EstacionamentoExistenteException(String message) {
		super(message);
	}

}
