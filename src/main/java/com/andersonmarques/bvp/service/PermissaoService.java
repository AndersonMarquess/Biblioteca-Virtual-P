package com.andersonmarques.bvp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.PermissaoRepository;

@Service
public class PermissaoService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	public void adicionar(Permissao permissao) {
		permissaoRepository.save(permissao);
	}

	public void adicionarTodasAsPermissoesNoUsuario(Usuario usuario, Set<Permissao> permissoes) {
		
		for(Permissao p: permissoes) {
			Optional<Permissao> permissaoResult = permissaoRepository.findByNomePermissao(p.getNomePermissao());
			
			if(permissaoResult.isPresent()) {
				System.out.println("[ Permissão recuperada ] - "+permissaoResult.get().getId());
				adicionarPermissaoNoUsuario(usuario, permissaoResult.get());
			} else {
				adicionarPermissaoNoUsuario(usuario, p);
			}
		}
	}

	private void adicionarPermissaoNoUsuario(Usuario usuario, Permissao permissao) {
		permissao.adicionarUsuario(usuario);
		System.out.println(String.format("[ %s ] - [ %d ]", permissao.getNomePermissao(),permissao.getUsuarios().size()));
		permissaoRepository.save(permissao);
	}
	
	public List<Permissao> buscarPermissoesPorIdUsuario(String idUsuario) {
		return permissaoRepository.findAllByUsuarios(idUsuario);
	}
}
