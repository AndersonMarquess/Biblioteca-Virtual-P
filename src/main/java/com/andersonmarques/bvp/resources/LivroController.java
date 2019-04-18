package com.andersonmarques.bvp.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.service.LivroService;

import reactor.core.publisher.Flux;

@RestController
public class LivroController {

	private final String V1_BASE_PATH = "v1/livro";

	@Autowired
	private LivroService livroService;

	@GetMapping(path = V1_BASE_PATH + "/all", produces = { "application/json" })
	public Flux<Livro> buscarTodos() {
		return Flux.fromIterable(livroService.buscarTodos());
	}

}
