package com.andersonmarques.bvp.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andersonmarques.bvp.exception.UsuarioSemAutorizacaoException;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.security.EndpointUtil;
import com.andersonmarques.bvp.service.LivroService;
import com.andersonmarques.bvp.service.UsuarioService;

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
		return ResponseEntity.ok(Flux.fromIterable(livroService.buscarTodos()));
	}

	@PostMapping(path = V1_BASE_PATH, produces = { "application/json" })
	public ResponseEntity<Mono<Livro>> adicionar(@RequestBody Livro livro) {
		Livro livroResposta = livroService.adicionar(livro);
		return ResponseEntity.ok(Mono.just(livroResposta));
	}

	@GetMapping(path = V1_BASE_PATH + "/all/{id}", produces = { "application/json" })
	public ResponseEntity<Flux<Livro>> buscarLivrosDoUsuarioPorId(@PathVariable("id") String id) {
		List<Livro> livros = livroService.buscarLivrosPorIdUsuario(id);
		return ResponseEntity.ok(Flux.fromIterable(livros));
	}

	@DeleteMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public ResponseEntity<Mono<String>> removerPorId(@PathVariable("id") String id) {
		Livro livro = livroService.buscarPorId(id);
		if (!EndpointUtil.isUsuarioPermitido(livro.getIdDonoLivro(), usuarioService)) {
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		livroService.removerPorId(id);
		return ResponseEntity.ok(Mono.empty());
	}

	@PutMapping(path = V1_BASE_PATH, produces = { "application/json" })
	public ResponseEntity<Mono<String>> atualizar(@RequestBody Livro livro) {
		if (!EndpointUtil.isUsuarioPermitido(livro.getIdDonoLivro(), usuarioService)) {
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		Livro livroAtualizado = livroService.atualizar(livro);
		return ResponseEntity.ok(Mono.just(livroAtualizado.gerarJSON()));
	}
}
