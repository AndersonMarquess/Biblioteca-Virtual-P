package com.andersonmarques.bvp.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.repository.PermissaoRepository;

@Service
public class PermissaoService {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	public void adicionar(Permissao permissao) {
		permissaoRepository.save(permissao);
	}

	public void adicionarTodas(Set<Permissao> permissoes) {
		permissaoRepository.saveAll(permissoes);
	}
}
