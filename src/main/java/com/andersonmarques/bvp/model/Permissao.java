package com.andersonmarques.bvp.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Permissao {

	@Id
	private String id;
	@Indexed(unique = true)
	private String nomePermissao;

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
}
