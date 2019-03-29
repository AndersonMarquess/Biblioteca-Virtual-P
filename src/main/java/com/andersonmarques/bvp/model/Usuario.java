package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andersonmarques.bvp.model.enums.Tipo;

@Document
public class Usuario {
	
	@Id
	private String id;
	private String nome;
	private String senha;
    private String email;
    @DBRef(lazy=false)
	private List<Contato> contatos = new ArrayList<>();

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

	public void adicionarContato(Contato contato) {
		contatos.add(contato);
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public Contato getContatoPorTipo(Tipo tipo) {
		return contatos.stream().filter(c -> c.getTipo() == tipo).findFirst().get();
	}
}
