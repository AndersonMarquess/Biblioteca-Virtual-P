package com.andersonmarques.bvp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Permissao;
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
		criptografarSenha(usuario);
		adicionarPermissaoPadrao(usuario);
		
		Usuario userRecuperado = usuarioRepository.save(usuario);

		for (Contato c : usuario.getContatos()) {
			contatoService.adicionar(c);
		}

		permissaoService.adicionarTodasAsPermissoesNoUsuario(usuario, usuario.getPermissoes());

		return userRecuperado;
	}

	private void criptografarSenha(Usuario usuario) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
	}

	private void adicionarPermissaoPadrao(Usuario usuario) {
		if (usuario.getPermissoes().stream().noneMatch(p -> p.getNomePermissao().equals("ROLE_USER"))) {
			usuario.adicionarPermissao(new Permissao("USER"));
			System.out.println(String.format("Adicionado permissão padrão no usuário: [ %s ] ", usuario.getNome()));
		}
	}

	public Usuario buscarUsuarioPorId(String id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		if(!usuario.isPresent()) {
			throw new IllegalArgumentException("Id inválido");
		}
		
		usuario.get().setPermissoes(permissaoService.buscarPermissoesPorIdUsuario(id));
		
		return usuario.get();
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
