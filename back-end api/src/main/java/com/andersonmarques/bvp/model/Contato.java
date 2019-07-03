package com.andersonmarques.bvp.model;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	@NotBlank(message = "O endereço de contato é obrigatório.")
	@Size(min = 3, max = 80, message = "O endereço de contato deve conter entre {min} e {max} caracteres.")
	private String endereco;

	public Contato() {
		this.id = UUID.randomUUID().toString();
	}

	public Contato(String endereco, Tipo tipo) {
		this();
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
