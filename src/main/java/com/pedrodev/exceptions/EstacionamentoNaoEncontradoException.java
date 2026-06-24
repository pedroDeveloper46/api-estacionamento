package com.pedrodev.exceptions;

@SuppressWarnings("serial")
public class EstacionamentoNaoEncontradoException extends RuntimeException {

	public EstacionamentoNaoEncontradoException(String message) {
		super(message);
	}
}

