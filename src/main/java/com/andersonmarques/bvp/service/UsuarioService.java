package com.andersonmarques.bvp.service;

import org.springframework.beans.factory.annotation.Autowired;
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
		return usuarioRepository.findById(id).
				orElseThrow(() -> new IllegalArgumentException("ID não encontrado"));
	}
	
	public Usuario buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findUserByEmail(email).
				orElseThrow(() -> new IllegalArgumentException("Email não encontrado"));
	}
}
