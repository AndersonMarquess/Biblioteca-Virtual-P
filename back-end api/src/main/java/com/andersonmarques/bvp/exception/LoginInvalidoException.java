package com.andersonmarques.bvp.exception;

public class LoginInvalidoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public LoginInvalidoException(String msg) {
		super(msg);
	}
}