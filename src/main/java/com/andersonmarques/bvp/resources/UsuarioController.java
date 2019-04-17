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

import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.service.UsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UsuarioController {

	private final String V1_BASE_PATH = "v1/usuario";

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(path = V1_BASE_PATH + "/all", produces = { "application/json" })
	public Flux<Usuario> listarTodos() {
		List<Usuario> usuarios = usuarioService.buscarTodos();
		return Flux.fromIterable(usuarios);
	}

	@PostMapping(path = V1_BASE_PATH)
	public Mono<Usuario> adicionar(@RequestBody Usuario usuario) {
		Usuario usuarioResposta = usuarioService.adicionar(usuario);
		return Mono.just(usuarioResposta);
	}

	@GetMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public Mono<Usuario> buscarInfoPorId(@PathVariable("id") String id) {
		Usuario usuarioResposta = usuarioService.buscarUsuarioPorId(id);
		return Mono.just(usuarioResposta);
	}
	
	@DeleteMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public Mono<ResponseEntity<Void>> removerPorId(@PathVariable("id") String id) {
		usuarioService.removerPorId(id);
		return Mono.just(ResponseEntity.ok().build());
	}
	
	@PutMapping(path=V1_BASE_PATH, produces = { "application/json" })
	public Mono<Usuario> atualizar(@RequestBody Usuario usuario) {
		Usuario usuarioAtualizado = usuarioService.atualizar(usuario);
		return Mono.just(usuarioAtualizado);
	}
}
