package com.andersonmarques.bvp.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Classe usada para informar dados de login.
 */
public class CredenciaisLogin {

	private String username;
	private String password;


	public CredenciaisLogin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UsernamePasswordAuthenticationToken getToken() {
		// Verificar se a senha neste ponto precisa de encode
		return new UsernamePasswordAuthenticationToken(this.username, this.password);
	}
}