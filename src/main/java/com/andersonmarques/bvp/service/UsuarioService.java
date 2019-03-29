package com.andersonmarques.bvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public void adicionar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	public Usuario buscarUsuarioPorId(String id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id inválido"));
	}
	
	public Usuario buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findOne(Example.of(new Usuario(null, null, email)))
				.orElseThrow(() -> new IllegalArgumentException("Usuário não contrado"));
	}
}
