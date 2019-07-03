package com.andersonmarques.bvp.exception;

public class CategoriaDuplicadaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CategoriaDuplicadaException(String message) {
		super(message);
	}
}
