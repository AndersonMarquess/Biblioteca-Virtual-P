package com.andersonmarques.bvp.resources;

import java.util.List;

import javax.validation.Valid;

import com.andersonmarques.bvp.exception.UsuarioSemAutorizacaoException;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.LivroComContatoDTO;
import com.andersonmarques.bvp.security.EndpointUtil;
import com.andersonmarques.bvp.service.LivroService;
import com.andersonmarques.bvp.service.LogService;
import com.andersonmarques.bvp.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class LivroController {

	private final String V1_BASE_PATH = "v1/livro";

	@Autowired
	private LivroService livroService;
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(path = V1_BASE_PATH + "/all", produces = { "application/json" })
	public ResponseEntity<Flux<Livro>> buscarTodos() {
		LogService.imprimirAcaoDoUsuario("buscar todos os livros, sem paginação.");
		return ResponseEntity.ok(Flux.fromIterable(livroService.buscarTodos()));
	}

	@GetMapping(path = V1_BASE_PATH + "/allpg", produces = { "application/json" })
	public ResponseEntity<Flux<LivroComContatoDTO>> buscarTodos(Pageable paginacao) {
		LogService.imprimirAcaoDoUsuario("buscar todos os livros com contato DTO.");
		return ResponseEntity.ok(Flux.fromIterable(livroService.buscarTodosComContatoDoUsuario(paginacao)));
	}

	@PostMapping(path = V1_BASE_PATH, produces = { "application/json" })
	public ResponseEntity<Mono<Livro>> adicionar(@Valid @RequestBody Livro livro) {
		LogService.imprimirAcaoDoUsuario("adicionar livro com título: "+livro.getTitulo());
		Livro livroResposta = livroService.adicionar(livro);
		return ResponseEntity.ok(Mono.just(livroResposta));
	}

	@GetMapping(path = V1_BASE_PATH + "/all/{id}", produces = { "application/json" })
	public ResponseEntity<Flux<Livro>> buscarLivrosDoUsuarioPorId(@PathVariable("id") String id) {
		LogService.imprimirAcaoDoUsuario("buscar todos os livros do usuário com id: "+id);
		List<Livro> livros = livroService.buscarLivrosPorIdUsuario(id);
		return ResponseEntity.ok(Flux.fromIterable(livros));
	}

	@DeleteMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public ResponseEntity<Mono<String>> removerPorId(@PathVariable("id") String id) {
		LogService.imprimirAcaoDoUsuario("remover usuário com id: "+id);
		Livro livro = livroService.buscarPorId(id);	
		
		if (!EndpointUtil.isUsuarioPermitido(livro.getIdDonoLivro(), usuarioService)) {
			LogService.imprimirAcaoDoUsuario("não possui permissão para remover livro com id: "+id);
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		
		livroService.removerPorId(id);
		return ResponseEntity.ok(Mono.empty());
	}

	@PutMapping(path = V1_BASE_PATH, produces = { "application/json" })
	public ResponseEntity<Mono<String>> atualizar(@Valid @RequestBody Livro livro) {
		LogService.imprimirAcaoDoUsuario("atualizar livro com id: "+livro.getId());
		
		if (!EndpointUtil.isUsuarioPermitido(livro.getIdDonoLivro(), usuarioService)) {
			LogService.imprimirAcaoDoUsuario("não possui permissão para atualizar livro com id: "+livro.getId());
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		
		Livro livroAtualizado = livroService.atualizar(livro);
		return ResponseEntity.ok(Mono.just(livroAtualizado.gerarJSON()));
	}
}
