package com.andersonmarques.bvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.repository.LivroRepository;

@Service
public class LivroService {
	
	@Autowired
	private LivroRepository livroRepository;

	public void adicionar(Livro livro) {
		livroRepository.save(livro);
	}

	public Livro buscarPorTitulo(String titulo) {
		return livroRepository.findFirstLivroByTituloLike(titulo);
	}

	public void removerPorId(Integer id) {
		livroRepository.deleteById(id);
	}
}
