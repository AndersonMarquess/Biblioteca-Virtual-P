package com.andersonmarques.bvp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.andersonmarques.bvp.model.enums.Tipo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document
public class Usuario {

	@Id
	private String id;
	@NotBlank(message = "O nome é obrigatório.")
	private String nome;
	@Size(min = 3, max = 80, message = "A senha deve conter entre {min} e {max} caracteres.")
	@NotBlank(message = "A senha é obrigatória.")
	private String senha;
	@Email(message = "O campo de e-mail é obrigatório.")
	private String email;
	@Valid
	@DBRef(lazy = false)
	@NotEmpty(message = "Informe pelo menos um meio de contato.")
	private List<Contato> contatos = new ArrayList<>();
	@DBRef(lazy = true)
	private Set<Permissao> permissoes = new HashSet<>();

	public Usuario() {
		this.id = UUID.randomUUID().toString();
	}

	public Usuario(String nome, String senha, String email) {
		this();
		this.nome = nome;
		this.senha = encodeSenha(senha);
		this.email = email;
	}

	/**
	 * Faz o encode da senha com o BCrypt se a senha não começar com "$2a$10$"
	 * padrão para o: $2a$[Salt]$[Base64Hash].
	 * 
	 * 
	 * @param senha
	 * @return
	 */
	private String encodeSenha(String senha) {
		if (senha.startsWith("$2a$10$") && senha.length() == 60)
			return senha;
		return new BCryptPasswordEncoder().encode(senha);
	}

	public String getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSenha(String senha) {
		this.senha = encodeSenha(senha);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public String getSenha() {
		return senha;
	}

	public String getEmail() {
		return email;
	}

	public void adicionarContato(Contato contato) {
		contatos.add(contato);
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public Contato getContatoPorTipo(Tipo tipo) {
		return contatos.stream().filter(c -> c.getTipoLiteral() == tipo).findFirst().get();
	}

	public void adicionarPermissao(Permissao... permissao) {
		permissoes.addAll(Arrays.asList(permissao));
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String gerarJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Erro ao gerar json";
	}

	@Override
	public String toString() {
		return String.format("Usuario [id=%s, nome=%s, senha=%s, email=%s, contatos=%s, permissoes=%s]", id, nome,
				senha, email, contatos, permissoes);
	}
}
