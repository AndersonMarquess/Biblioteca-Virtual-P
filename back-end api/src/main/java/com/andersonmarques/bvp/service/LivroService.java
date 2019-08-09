package com.andersonmarques.bvp.service;

import java.util.List;

import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.repository.LivroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private CategoriaService categoriaService;

	@Cacheable("livroBuscarPorTitulo")
	public Livro buscarPorTitulo(String titulo) {
		System.out.println("Consulta no banco - livro por titulo " + titulo);
		return livroRepository.findFirstLivroByTituloContainsIgnoreCase(titulo);
	}

	@Cacheable("livroBuscarTodos")
	public List<Livro> buscarTodos() {
		System.out.println("Consulta no banco - todos os livros");
		return livroRepository.findAll();
	}

	@Cacheable("livroBuscarTodosPaginado")
	public Page<Livro> buscarTodos(Pageable paginacao) {
		System.out.println("Consulta no banco - todos os livros com paginação");
		return livroRepository.findAll(paginacao);
	}

	@Cacheable("livroBuscarTodosPorIdUsuario")
	public List<Livro> buscarLivrosPorIdUsuario(String id) {
		System.out.println("Consulta no banco - livros por id usuario " + id);
		return livroRepository.findAllByIdDonoLivro(id);
	}

	@Cacheable("livroBuscarPorId")
	public Livro buscarPorId(String id) {
		System.out.println("Consulta no banco - livro por id " + id);
		return livroRepository.findById(id).get();
	}

	@CacheEvict(cacheNames = { "livroBuscarPorTitulo", "livroBuscarTodos", "livroBuscarTodosPaginado",
			"livroBuscarTodosPorIdUsuario", "livroBuscarPorId" }, allEntries = true)
	public Livro adicionar(Livro livro) {
		categoriaService.adicionarTodasAsCategoriaNoLivro(livro, livro.getCategorias());
		return livroRepository.save(livro);
	}

	@CacheEvict(cacheNames = { "livroBuscarPorTitulo", "livroBuscarTodos", "livroBuscarTodosPaginado",
			"livroBuscarTodosPorIdUsuario", "livroBuscarPorId" }, allEntries = true)
	public void removerPorId(String id) {
		livroRepository.deleteById(id);
	}

	@CacheEvict(cacheNames = { "livroBuscarPorTitulo", "livroBuscarTodos", "livroBuscarTodosPaginado",
			"livroBuscarTodosPorIdUsuario", "livroBuscarPorId" }, allEntries = true)
	void removerLivrosDoUsuarioComId(String id) {
		livroRepository.deleteAllByIdDonoLivro(id);
	}

	@CacheEvict(cacheNames = { "livroBuscarPorTitulo", "livroBuscarTodos", "livroBuscarTodosPaginado",
			"livroBuscarTodosPorIdUsuario", "livroBuscarPorId" }, allEntries = true)
	public Livro atualizar(Livro livro) {
		categoriaService.adicionarTodasAsCategoriaNoLivro(livro, livro.getCategorias());
		return livroRepository.save(livro);
	}
}
