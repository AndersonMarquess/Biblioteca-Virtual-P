package com.andersonmarques.bvp.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Permissao {

	@Id
	private String id;
	@Indexed(unique = true)
	private String nomePermissao;
	@DBRef(lazy=true)
	private Set<Usuario> usuarios = new HashSet<>();

	public Permissao() {
		this.id = UUID.randomUUID().toString();
	}

	public Permissao(String nomePermissao) {
		this();
		padronizarNomePermissao(nomePermissao);
	}

	public String getId() {
		return id;
	}

	public String getNomePermissao() {
		return nomePermissao;
	}

	public void setNomePermissao(String nomePermissao) {
		padronizarNomePermissao(nomePermissao);
	}
	
	public void adicionarUsuario(Usuario usuario) {
		usuarios.add(usuario);
	}

	@JsonIgnore
	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	private void padronizarNomePermissao(String nomePermissao) {
		if (nomePermissao.toUpperCase().startsWith("ROLE_")) {
			this.nomePermissao = nomePermissao.toUpperCase();
		} else {
			this.nomePermissao = "ROLE_" + nomePermissao.toUpperCase();
		}
	}
}
