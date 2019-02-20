package com.andersonmarques.bvp.model;

public class Usuario {
	
	private String nome;
	private String senha;
	private String email;

	public Usuario() {}
	
	public Usuario(String nome, String senha, String email) {
		this.nome = nome;
		this.senha = senha;
		this.email = email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public String getSenha() {
		return senha;
	}

	public String getEmail() {
		return email;
	}
}
