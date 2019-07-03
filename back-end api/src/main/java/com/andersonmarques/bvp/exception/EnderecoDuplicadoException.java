package com.andersonmarques.bvp.exception;

public class EnderecoDuplicadoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EnderecoDuplicadoException() {
	}

	public EnderecoDuplicadoException(String message) {
		super(message);
	}
}
