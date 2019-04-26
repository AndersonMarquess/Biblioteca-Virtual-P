package com.andersonmarques.bvp.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.PermissaoRepository;

@Service
public class PermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;

	@Cacheable("permissaoBuscarTodasPorIdUsuario")
	public Set<Permissao> buscarPermissoesPorIdUsuario(String idUsuario) {
		System.out.println("Consulta no banco - permissoes por id usuario " + idUsuario);
		return permissaoRepository.findAllByUsuariosId(idUsuario);
	}

	@CacheEvict(cacheNames = "permissaoBuscarTodasPorIdUsuario", allEntries = true)
	public void adicionar(Permissao permissao) {
		permissaoRepository.save(permissao);
	}

	void adicionarTodasAsPermissoesNoUsuario(Usuario usuario, Set<Permissao> permissoes) {
		for (Permissao p : permissoes) {
			Optional<Permissao> permissaoResult = permissaoRepository.findByNomePermissao(p.getNomePermissao());

			if (permissaoResult.isPresent()) {
				System.out.println("[ Permissão recuperada ] - " + permissaoResult.get().getId());
				adicionarPermissaoNoUsuario(usuario, permissaoResult.get());
			} else {
				adicionarPermissaoNoUsuario(usuario, p);
			}
		}
	}

	@CacheEvict(cacheNames = "permissaoBuscarTodasPorIdUsuario", allEntries = true)
	private void adicionarPermissaoNoUsuario(Usuario usuario, Permissao permissao) {
		permissao.adicionarUsuario(usuario);
		System.out.println(
				String.format("[ %s ] - [ %d ]", permissao.getNomePermissao(), permissao.getUsuarios().size()));
		permissaoRepository.save(permissao);
	}
}
