package com.andersonmarques.bvp.model;

import com.andersonmarques.bvp.model.enums.Tipo;

public class Contato {
	
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
