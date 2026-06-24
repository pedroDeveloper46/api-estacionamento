package com.pedrodev.exceptions;

@SuppressWarnings("serial")
public class EstacionamentoNaoAtivoException extends RuntimeException {
	
	public EstacionamentoNaoAtivoException(String message) {
		super(message);
	}

}
