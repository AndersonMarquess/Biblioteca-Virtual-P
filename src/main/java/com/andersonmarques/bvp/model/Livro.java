package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;

@Document
public class Livro {

	@Id
	private String id;
	private String isbn;
	private String titulo;
	private String descricao;
	private String urlCapa;
	@DBRef(lazy = false)
	private List<Categoria> categorias = new ArrayList<>();
	private String idDonoLivro;

	public Livro() {
		this.id = UUID.randomUUID().toString();
	}

	public Livro(String isbn, String titulo, String descricao, String urlCapa, String idDonoLivro) {
		this();
		this.isbn = isbn;
		this.titulo = titulo;
		this.descricao = descricao;
		this.urlCapa = urlCapa;
		this.idDonoLivro = idDonoLivro;
	}

	public String getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getUrlCapa() {
		return urlCapa;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUrlCapa(String urlCapa) {
		this.urlCapa = urlCapa;
	}

	public void adicionarCategoria(Categoria... categoria) {
		for (Categoria cat : categoria) {
			if (categorias.contains(cat)) {
				throw new CategoriaDuplicadaException("A Categoria [" + cat + "] já foi adicionada.");
			}
			categorias.add(cat);
		}
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public String getIdDonoLivro() {
		return idDonoLivro;
	}

	public void setIdDonoLivro(String idDonoLivro) {
		this.idDonoLivro = idDonoLivro;
	}
}
