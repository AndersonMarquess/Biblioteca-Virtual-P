package com.andersonmarques.bvp.dto;

import java.util.ArrayList;
import java.util.List;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Usuario;

public class UsuarioDTO {

	private String id;
	private String nome;
	private List<Contato> contatos;

	public UsuarioDTO() {}
	
	public UsuarioDTO(Usuario usuario) {
		id = usuario.getId();
		nome = usuario.getNome();
		contatos = new ArrayList<>(usuario.getContatos());
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public List<Contato> getContatos() {
		return contatos;
	}
}
