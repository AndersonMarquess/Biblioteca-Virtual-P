package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document
public class Livro {

	@Id
	private String id;
	private String isbn;
	@NotEmpty(message = "O campo de título é obrigatório.")
	@Size(min = 5, max = 150, message = "O título deve conter entre {min} e {max} caracteres.")
	private String titulo;
	private String descricao;
	@Size(max = 250, message = "A url da capa do livro não pode possuir mais que {max} caracteres.")
	private String urlCapa;
	@DBRef(lazy = false)
	private List<Categoria> categorias = new ArrayList<>();
	@NotEmpty(message = "O id do dono do livro é obrigatório.")
	@Size(min = 36, max = 36, message = "O id do dono do livro deve conter {min} caracteres.")
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

	public String gerarJSON() {
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Livro livro = (Livro) o;
		return id.equals(livro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
