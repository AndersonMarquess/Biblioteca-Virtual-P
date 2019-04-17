package com.andersonmarques.bvp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.exception.EnderecoDuplicadoException;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.repository.ContatoRepository;

@Service
public class ContatoService {

	@Autowired
	private ContatoRepository contatoRepository;

	public Contato adicionar(Contato contato) {
		if (encontrarPorEndereco(contato.getEndereco()) != null) {
			throw new EnderecoDuplicadoException(String.format(
					"Impossível adicionar o mesmo endereço de contato mais de uma vez. [ %s ]", contato.getEndereco()));
		}

		return contatoRepository.save(contato);
	}

	public Contato encontrarPorEndereco(String endereco) {
		return contatoRepository.findByEndereco(endereco);
	}

	public void removerPorId(String id) {
		contatoRepository.deleteById(id);
	}

	public void atualizar(List<Contato> contatos) {
		contatoRepository.saveAll(contatos);
	}
}
