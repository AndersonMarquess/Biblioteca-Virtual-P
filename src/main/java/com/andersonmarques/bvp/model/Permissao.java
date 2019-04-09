package com.andersonmarques.bvp.model;

import java.util.HashSet;
import java.util.Objects;
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
	}

	public Permissao(String nomePermissao) {
		id = UUID.randomUUID().toString();
		
		if (nomePermissao.toUpperCase().startsWith("ROLE_")) {
			this.nomePermissao = nomePermissao.toUpperCase();
		} else {
			this.nomePermissao = "ROLE_" + nomePermissao.toUpperCase();
		}
	}

	public String getId() {
		return id;
	}

	public String getNomePermissao() {
		return nomePermissao;
	}

	public void setNomePermissao(String nomePermissao) {
		this.nomePermissao = nomePermissao;
	}
	
	public void adicionarUsuario(Usuario usuario) {
		usuarios.add(usuario);
	}

	@JsonIgnore
	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nomePermissao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permissao other = (Permissao) obj;
		return Objects.equals(id, other.id) && Objects.equals(nomePermissao, other.nomePermissao);
	}
}
