package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;

public class Livro {
	
	private String isbn;
	private String nome;
	private String descricao;
	private String urlCapa;
	private List<Categoria> categorias = new ArrayList<>();

	public Livro() {}
	
	public Livro(String isbn, String nome, String descricao, String urlCapa) {
		this.isbn = isbn;
		this.nome = nome;
		this.descricao = descricao;
		this.urlCapa = urlCapa;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUrlCapa(String urlCapa) {
		this.urlCapa = urlCapa;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getUrlCapa() {
		return urlCapa;
	}

	public void adicionarCategoria(Categoria categoria) {
		if(categorias.contains(categoria)) {
			throw new CategoriaDuplicadaException("A Categoria ["+categoria.getNome()+"] já foi adicionada.");
		}
		
		categorias.add(categoria);
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}
}
