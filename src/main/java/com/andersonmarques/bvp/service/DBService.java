package com.andersonmarques.bvp.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;
import com.andersonmarques.bvp.repository.CategoriaRepository;
import com.andersonmarques.bvp.repository.ContatoRepository;
import com.andersonmarques.bvp.repository.PermissaoRepository;
import com.andersonmarques.bvp.repository.UsuarioRepository;

@Component
public class DBService implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PermissaoRepository permissaoRepository;
	@Autowired
	private ContatoRepository contatoRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private LivroService livroService;
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void run(String... args) throws Exception {
		popularBanco();
		criarAdminPadrao();
	}

	private void popularBanco() {
		// Dropa o banco
		mongoTemplate.getDb().drop();

		Usuario pessoa2 = new Usuario("necronomicon", "123", "necronomicon@email.com");
		pessoa2.adicionarContato(new Contato("necrono@fb.com", Tipo.FACEBOOK));
		pessoa2.adicionarPermissao(new Permissao("USER"), new Permissao("MASTER"));

		Usuario pessoa3 = new Usuario("faraday", "password", "faraday@email.com");
		pessoa3.adicionarContato(new Contato("faraday@fb.com", Tipo.FACEBOOK));
		pessoa3.adicionarPermissao(new Permissao("USER"));

		pessoa2 = usuarioService.adicionar(pessoa2);
		pessoa3 = usuarioService.adicionar(pessoa3);

		Livro livro2 = new Livro("421-351-571", "Y o último homem", "Descrição", "urlCapaDoLivro", pessoa2.getId());
		Livro livro3 = new Livro("51-456-4571", "Demolidor", "Descrição do livro", "urlCapaDoLivro", pessoa3.getId());

		Categoria ficcao = new Categoria("Ficção científica");
		Categoria aventura = new Categoria("Aventura");
		Categoria superHeroi = new Categoria("Super-herói");

		livro2.adicionarCategoria(ficcao, aventura);
		livro3.adicionarCategoria(superHeroi);

		livroService.adicionar(livro2);
		livroService.adicionar(livro3);
		categoriaRepository.saveAll(Arrays.asList(ficcao, aventura, superHeroi));
	}

	private void criarAdminPadrao() {
		if (!usuarioRepository.findUsuarioByEmail("admin@email.com").isPresent()) {
			System.out.println("Criando administrador padrão.");
			Usuario pessoa1 = new Usuario("admin", "password", "admin@email.com");
			pessoa1.adicionarPermissao(new Permissao("ADMIN"));

			Optional<Permissao> roleAdmin = permissaoRepository.findByNomePermissao("ROLE_ADMIN");

			if (!roleAdmin.isPresent()) {
				Permissao permissao = pessoa1.getPermissoes().stream().findFirst().get();
				permissao.adicionarUsuario(pessoa1);
				permissaoRepository.save(permissao);
			} else {
				roleAdmin.get().adicionarUsuario(pessoa1);
				permissaoRepository.save(roleAdmin.get());
			}

			usuarioRepository.save(pessoa1);
			contatoRepository.saveAll(pessoa1.getContatos());
		}
	}
}
