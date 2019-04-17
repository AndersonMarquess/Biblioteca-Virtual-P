package com.andersonmarques.bvp.model;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andersonmarques.bvp.model.enums.Tipo;

@Document
public class Contato {

	@Id
	private String id;
	private Integer tipo;
	@Indexed(unique = true)
	private String endereco;

	public Contato() {
		id = UUID.randomUUID().toString();
	}

	public Contato(String endereco, Tipo tipo) {
		id = UUID.randomUUID().toString();
		this.endereco = endereco;
		this.tipo = tipo.getId();
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

	public Tipo getTipoLiteral() {
		return Tipo.toEnum(tipo);
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = Tipo.toEnum(tipo).getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(endereco, id, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		return Objects.equals(endereco, other.endereco) && Objects.equals(id, other.id)
				&& Objects.equals(tipo, other.tipo);
	}
}
