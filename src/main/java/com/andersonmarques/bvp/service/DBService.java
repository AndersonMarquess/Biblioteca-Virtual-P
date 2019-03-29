package com.andersonmarques.bvp.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.CategoriaRepository;
import com.andersonmarques.bvp.repository.LivroRepository;
import com.andersonmarques.bvp.repository.UsuarioRepository;

@Component
public class DBService implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.getDb().drop();
		System.out.println(
				"Drop banco [ " + DBService.class.getSimpleName() + " ] Tentando persistir informações no banco");
		String senha = new BCryptPasswordEncoder().encode("123");
		usuarioRepository.save(new Usuario("anderson", senha, "email@email"));
		livro_categoria();
	}

	private void livro_categoria() {

		Livro l1 = new Livro("123-478-5555", "Livro Padrão 1", "Descrição do livro padrão 1", "urlCapaDoLivro");
		Livro l2 = new Livro("321-478-6666", "Livro Padrão 2", "Descrição do livro padrão 2", "urlCapaDoLivro");
		Livro l3 = new Livro("478-312-7777", "Livro Padrão 3 - Pro e UML", "Descrição sobre programção e uml",
				"urlCapaDoLivro");
		livroRepository.saveAll(Arrays.asList(l1, l2, l3));

		Categoria c1 = new Categoria("Programação");
		Categoria c2 = new Categoria("UML");
		categoriaRepository.saveAll(Arrays.asList(c1, c2));

		l1.adicionarCategoria(c1);
		l2.adicionarCategoria(c2);
		l3.adicionarCategoria(c1, c2);

		categoriaRepository.saveAll(Arrays.asList(c1, c2));
		livroRepository.saveAll(Arrays.asList(l1, l2, l3));
	}
}
