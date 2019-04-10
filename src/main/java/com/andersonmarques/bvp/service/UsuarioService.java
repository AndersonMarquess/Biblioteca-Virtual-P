package com.andersonmarques.bvp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ContatoService contatoService;
	@Autowired
	private PermissaoService permissaoService;

	public Usuario adicionar(Usuario usuario) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		Usuario userRecuperado = usuarioRepository.save(usuario);
	
		for (Contato c : usuario.getContatos()) {
			contatoService.adicionar(c);
		}
		
		permissaoService.adicionarTodasAsPermissoesNoUsuario(usuario, usuario.getPermissoes());

		return userRecuperado;
	}

	public Usuario buscarUsuarioPorId(String id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id inválido"));
	}
	
	public Usuario buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findUsuarioByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não contrado"));
	}

	public void removerPorId(String id) {
		Usuario usuario = buscarUsuarioPorId(id);
		usuarioRepository.deleteById(id);
		
		for (Contato c : usuario.getContatos()) {
			contatoService.removerPorId(c.getId());
		}
	}

	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
}
