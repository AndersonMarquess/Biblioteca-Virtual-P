package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;

public class LivroComContatoDTO {

	private Livro livro;
	private List<Contato> contatos = new ArrayList<>();

	public LivroComContatoDTO(Livro livro, List<Contato> contatos) {
		this.livro = livro;
		this.contatos = contatos;
	}

	public Livro getLivro() {
		return livro;
	}

	public List<Contato> getContatos() {
		return contatos;
	}
}