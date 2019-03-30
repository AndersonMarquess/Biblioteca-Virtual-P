package com.andersonmarques.bvp.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andersonmarques.bvp.model.enums.Tipo;

@Document
public class Contato {
	
	@Id
	private String id;
	private Tipo tipoContato;
	@Indexed(unique=true)
	private String endereco;

	public Contato() {}
	
	public Contato(String endereco, Tipo tipoContato) {
        id = UUID.randomUUID().toString();
		this.endereco = endereco;
		this.tipoContato = tipoContato;
	}
	
	public String getId() {
		return id;
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
