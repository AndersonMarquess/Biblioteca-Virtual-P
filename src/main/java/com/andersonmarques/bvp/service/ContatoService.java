package com.andersonmarques.bvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.repository.ContatoRepository;

@Service
public class ContatoService {
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	public void adicionar(Contato contato) {
		contatoRepository.save(contato);
	}

	public void removerPorId(String id) {
		contatoRepository.deleteById(id);
	}
}
