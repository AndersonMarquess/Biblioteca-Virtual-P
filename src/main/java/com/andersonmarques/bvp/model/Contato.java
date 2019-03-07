package com.andersonmarques.bvp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.andersonmarques.bvp.model.enums.Tipo;

@Entity
public class Contato {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private Tipo tipoContato;
	private String endereco;

	public Contato() {}
	
	public Contato(String endereco, Tipo tipoContato) {
		this.endereco = endereco;
		this.tipoContato = tipoContato;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	public Tipo getTipo() {
		return tipoContato;
	}
}
