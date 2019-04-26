package com.andersonmarques.bvp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	@Autowired
	private LivroService livroService;

	@Cacheable("usuarioBuscarTodos")
	public List<Usuario> buscarTodos() {
		System.out.println("Consulta no banco - user todos");
		return usuarioRepository.findAll();
	}

	@Cacheable("usuarioBuscarPorId")
	public Usuario buscarUsuarioPorId(String id) {
		System.out.println("Consulta no banco - user por id " + id);
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (!usuario.isPresent()) {
			throw new IllegalArgumentException("Id inválido");
		}

		usuario.get().setPermissoes(permissaoService.buscarPermissoesPorIdUsuario(id));

		return usuario.get();
	}

	@Cacheable("usuarioBuscarPorEmail")
	public Usuario buscarUsuarioPorEmail(String email) {
		System.out.println("Consulta no banco - user por email " + email);
		Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(email);

		if (!usuario.isPresent()) {
			throw new IllegalArgumentException("Usuário não encontrado");
		}

		usuario.get().setPermissoes(permissaoService.buscarPermissoesPorIdUsuario(usuario.get().getId()));

		return usuario.get();
	}

	/**
	 * Evita que usuários com e-mail repetidos seja adicionados.
	 *
	 * @param usuario
	 */
	private void verificarDisponibilidadeDeEmail(Usuario usuario) {
		Optional<Usuario> usuarioRecuperado = usuarioRepository.findUsuarioByEmail(usuario.getEmail());

		if (usuarioRecuperado.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Não é possível usar o e-mail: [ %s ]", usuario.getEmail()));
		}
	}

	@CacheEvict(cacheNames = { "usuarioBuscarTodos", "usuarioBuscarPorId", "usuarioBuscarPorEmail" }, allEntries = true)
	public Usuario adicionar(Usuario usuario) {
		verificarDisponibilidadeDeEmail(usuario);
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

	@CacheEvict(cacheNames = { "usuarioBuscarTodos", "usuarioBuscarPorId", "usuarioBuscarPorEmail" }, allEntries = true)
	public void removerPorId(String id) {
		Usuario usuario = buscarUsuarioPorId(id);
		usuarioRepository.deleteById(id);
		livroService.removerLivrosDoUsuarioComId(id);

		for (Contato c : usuario.getContatos()) {
			contatoService.removerPorId(c.getId());
		}
		invalidarUsuarioLogado(usuario);
	}

	/**
	 * Inválida o usuário que se remover.
	 *
	 * @param usuario
	 */
	private void invalidarUsuarioLogado(Usuario usuario) {
		Authentication usuarioAutenticado = SecurityContextHolder.getContext().getAuthentication();
		if (usuarioAutenticado != null && usuarioAutenticado.getName().equals(usuario.getEmail())) {
			usuarioAutenticado.setAuthenticated(false);
		}
	}

	@CacheEvict(cacheNames = { "usuarioBuscarTodos", "usuarioBuscarPorId", "usuarioBuscarPorEmail" }, allEntries = true)
	public Usuario atualizar(Usuario usuario) {
		contatoService.atualizar(usuario.getContatos());
		return usuarioRepository.save(usuario);
	}
}
