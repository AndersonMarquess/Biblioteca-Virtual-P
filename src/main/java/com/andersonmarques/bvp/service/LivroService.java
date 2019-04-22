package com.andersonmarques.bvp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.repository.LivroRepository;

@Service
public class LivroService {
	
	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private CategoriaService categoriaService;

	public Livro adicionar(Livro livro) {
		Livro livroRecuperado = livroRepository.save(livro);
		categoriaService.adicionarTodasAsCategoriaNoLivro(livro, livro.getCategorias());
		return livroRecuperado;
	}

	public Livro buscarPorTitulo(String titulo) {
		return livroRepository.findFirstLivroByTituloContainsIgnoreCase(titulo);
	}

	public void removerPorId(String id) {
		livroRepository.deleteById(id);
	}

	public List<Livro> buscarTodos() {
		return livroRepository.findAll();
	}

	public List<Livro> buscarLivrosPorIdUsuario(String id) {
		return livroRepository.findAllByIdDonoLivro(id);
	}
}
