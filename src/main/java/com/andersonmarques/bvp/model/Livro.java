package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;

@Entity
public class Livro {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String isbn;
	private String nome;
	private String descricao;
	private String urlCapa;
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
			joinColumns=@JoinColumn(name="FK_livro"),
			inverseJoinColumns=@JoinColumn(name="FK_categoria")
	)
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

	public void adicionarCategoria(Categoria... categoria) {
		for (Categoria cat : categoria) {
			if(categorias.contains(cat)) {
				throw new CategoriaDuplicadaException("A Categoria ["+cat+"]já foi adicionada.");
			}
			categorias.add(cat);
		}
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}
}
