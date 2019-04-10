package com.andersonmarques.bvp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public void adicionarTodasAsCategoriaNoLivro(Livro livro, List<Categoria> categorias) {
		
		for(Categoria c: categorias) {
			Optional<Categoria> categoriaResult = categoriaRepository.findByNome(c.getNome());
			
			if(categoriaResult.isPresent()) {
				System.out.println("[ Categoria ] "+categoriaResult.get().getNome());
				categoriaResult.get().adicionarLivro(livro);
				adicionarCategoria(categoriaResult.get());
			} else {
				c.adicionarLivro(livro);
				adicionarCategoria(c);
			}
		}
	}

	private void adicionarCategoria(Categoria categoria) {
		categoriaRepository.save(categoria);
	}
	
	public List<Categoria> buscarTodasCategoriasPorIdLivro(String id) {
		return categoriaRepository.findAllByLivrosId(id);
	}
}
