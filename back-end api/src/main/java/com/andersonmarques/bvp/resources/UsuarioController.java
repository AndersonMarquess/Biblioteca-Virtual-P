package com.andersonmarques.bvp.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andersonmarques.bvp.dto.UsuarioDTO;
import com.andersonmarques.bvp.exception.UsuarioSemAutorizacaoException;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.security.EndpointUtil;
import com.andersonmarques.bvp.service.UsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UsuarioController {

	private final String V1_BASE_PATH = "v1/usuario";

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(path = V1_BASE_PATH + "/all", produces = { "application/json" })
	public Flux<UsuarioDTO> listarTodos() {
		List<UsuarioDTO> usuariosDTO = usuarioService.buscarTodos().stream().map(UsuarioDTO::new)
				.collect(Collectors.toList());
		return Flux.fromIterable(usuariosDTO);
	}

	@PostMapping(path = V1_BASE_PATH)
	public Mono<Usuario> adicionar(@Valid @RequestBody Usuario usuario) {
		Usuario usuarioResposta = usuarioService.adicionar(usuario);
		return Mono.just(usuarioResposta);
	}

	@GetMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public ResponseEntity<Mono<String>> buscarInfoPorId(@PathVariable("id") String id) {
		if (!EndpointUtil.isUsuarioPermitido(id, usuarioService)) {
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		Usuario usuarioResposta = usuarioService.buscarUsuarioPorId(id);
		return ResponseEntity.ok(Mono.just(usuarioResposta.gerarJSON()));
	}

	@DeleteMapping(path = V1_BASE_PATH + "/{id}", produces = { "application/json" })
	public ResponseEntity<Mono<String>> removerPorId(@PathVariable("id") String id) {
		if (!EndpointUtil.isUsuarioPermitido(id, usuarioService)) {
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		usuarioService.removerPorId(id);
		return ResponseEntity.ok(Mono.empty());
	}

	@PutMapping(path = V1_BASE_PATH, produces = { "application/json" })
	public ResponseEntity<Mono<String>> atualizar(@Valid @RequestBody Usuario usuario) {
		if (!EndpointUtil.isUsuarioPermitido(usuario.getId(), usuarioService)) {
			throw new UsuarioSemAutorizacaoException("O usuário não tem autorização para buscar esta informação.");
		}
		Usuario usuarioAtualizado = usuarioService.atualizar(usuario);
		return ResponseEntity.ok(Mono.just(usuarioAtualizado.gerarJSON()));
	}
}
