package com.andersonmarques.bvp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import com.andersonmarques.bvp.model.enums.Tipo;

@Document
public class Contato {
	
	@Id
	private String id;
	private Tipo tipoContato;
	private String endereco;

	public Contato() {}
	
	public Contato(String endereco, Tipo tipoContato) {
        id = UUID.randomUUID().toString();
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
