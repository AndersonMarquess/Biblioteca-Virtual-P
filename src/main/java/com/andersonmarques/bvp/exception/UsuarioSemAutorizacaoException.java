package com.andersonmarques.bvp.exception;

public class UsuarioSemAutorizacaoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UsuarioSemAutorizacaoException(String message) {
		super(message);
	}
}
