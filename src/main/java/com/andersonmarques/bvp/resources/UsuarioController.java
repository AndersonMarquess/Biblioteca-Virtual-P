package com.andersonmarques.bvp.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
}
