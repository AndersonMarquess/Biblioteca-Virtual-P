package com.andersonmarques.bvp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.exception.EnderecoDuplicadoException;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.repository.ContatoRepository;

@Service
public class ContatoService {

	@Autowired
	private ContatoRepository contatoRepository;

	@Cacheable("contatoBuscarPorEndereco")
	public Contato encontrarPorEndereco(String endereco) {
		System.out.println("Consulta no banco - contato por endereco " + endereco);
		return contatoRepository.findByEndereco(endereco);
	}

	@CacheEvict(cacheNames = { "contatoBuscarPorEndereco" }, allEntries = true)
	Contato adicionar(Contato contato) {
		if (encontrarPorEndereco(contato.getEndereco()) != null) {
			throw new EnderecoDuplicadoException(String.format(
					"Impossível adicionar o mesmo endereço de contato mais de uma vez. [ %s ]", contato.getEndereco()));
		}

		return contatoRepository.save(contato);
	}

	@CacheEvict(cacheNames = { "contatoBuscarPorEndereco" }, allEntries = true)
	void removerPorId(String id) {
		contatoRepository.deleteById(id);
	}

	@CacheEvict(cacheNames = { "contatoBuscarPorEndereco" }, allEntries = true)
	void atualizar(List<Contato> contatos) {
		contatoRepository.saveAll(contatos);
	}
}
