package com.andersonmarques.bvp.exception;

public class NomeDeUsuarioNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NomeDeUsuarioNaoEncontradoException() { }

	public NomeDeUsuarioNaoEncontradoException(String message) {
		super(message);
	}

}
