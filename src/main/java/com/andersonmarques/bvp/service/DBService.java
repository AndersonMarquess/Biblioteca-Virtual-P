package com.andersonmarques.bvp.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;
import com.andersonmarques.bvp.repository.CategoriaRepository;

@Component
public class DBService implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private LivroService livroService;
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.getDb().drop();
		System.out.println(
				String.format("Drop Banco [ %s ] tentando persistir informações", DBService.class.getSimpleName()));
		popularBanco();
	}

	private void popularBanco() {
		criarUsuarios();
		criarLivros();
	}

	private void criarLivros() {
		Livro livro1 = new Livro("351-456-4571", "Dinastia M", "Descrição do livro", "urlCapaDoLivro");
		Livro livro2 = new Livro("421-351-571", "Y o último homem", "Descrição do livro", "urlCapaDoLivro");
		Livro livro3 = new Livro("51-456-4571", "Demolidor", "Descrição do livro", "urlCapaDoLivro");

		Categoria ficcao = new Categoria("Ficção científica");
		Categoria aventura = new Categoria("Aventura");
		Categoria superHeroi = new Categoria("Super-herói");

		livro1.adicionarCategoria(superHeroi);
		livro2.adicionarCategoria(ficcao, aventura);
		livro3.adicionarCategoria(superHeroi);

		livroService.adicionar(livro1);
		livroService.adicionar(livro2);
		livroService.adicionar(livro3);
		categoriaRepository.saveAll(Arrays.asList(ficcao, aventura, superHeroi));
	}

	private void criarUsuarios() {
		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();

		Usuario pessoa1 = new Usuario("admin", enconder.encode("password"), "admin@email.com");
		pessoa1.adicionarContato(new Contato("admin_social@rede.com", Tipo.TWITTER));
		pessoa1.adicionarPermissao(new Permissao("USER"), new Permissao("ADMIN"));
		
		Usuario pessoa2 = new Usuario("necronomicon", enconder.encode("password"), "necronomicon@email.com");
		pessoa2.adicionarContato(new Contato("necrono@micon.com", Tipo.TWITTER));
		pessoa2.adicionarPermissao(new Permissao("USER"), new Permissao("MASTER"));

		Usuario pessoa3 = new Usuario("faraday", enconder.encode("password"), "faraday@email.com");
		pessoa3.adicionarContato(new Contato("faraday@twitter.com", Tipo.TWITTER));
		pessoa3.adicionarPermissao(new Permissao("USER"), new Permissao("ADMIN"));

		usuarioService.adicionar(pessoa1);
		usuarioService.adicionar(pessoa2);
		usuarioService.adicionar(pessoa3);
	}
}
