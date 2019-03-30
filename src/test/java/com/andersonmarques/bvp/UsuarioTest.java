package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;
import com.andersonmarques.bvp.service.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {

	private Usuario user;
	@Autowired
	private UsuarioService usuarioService;

	@Before
	public void instanciarObjetos() {
		user = new Usuario("Anderson", "123", "email@contato.com");
	}

	@Test
	public void criarUsuarioComSucesso() {
		user.setNome("Anderson");
		user.setSenha("123");
		user.setEmail("email@contato.com");
	}

	@Test
	public void criarUsuarioEPegarInformacoes() {
		assertEquals("Anderson", user.getNome());
		assertEquals("123", user.getSenha());
		assertEquals("email@contato.com", user.getEmail());
	}

	@Test
	public void criarDoisUsuariosEPegarInformacoes() {
		assertEquals("Anderson", user.getNome());
		assertEquals("123", user.getSenha());
		assertEquals("email@contato.com", user.getEmail());

		Usuario user2 = new Usuario("Pedro", "321", "pedro@contato.com");
		assertEquals("Pedro", user2.getNome());
		assertEquals("321", user2.getSenha());
		assertEquals("pedro@contato.com", user2.getEmail());
	}

	@Test
	public void adicionarUmContatoAoUsuario() {
		Contato contato = new Contato();
		user.adicionarContato(contato);
	}

	@Test
	public void adicionarERecuperarUmContatoDoUsuario() {
		Contato contato = new Contato();
		user.adicionarContato(contato);

		assertTrue(user.getContatos().indexOf(contato) >= 0);
	}

	@Test
	public void adicionarERecuperarDadosDeContatoDoUsuario() {
		Contato contato = new Contato();
		contato.setEndereco("contato@twitter.com");
		user.adicionarContato(contato);

		assertTrue(user.getContatos().get(0).getEndereco().contains("contato@twitter.com"));
	}

	@Test
	public void recuperarUmContatoDoUsuarioPorTipoDeContato() {
		Contato contato = new Contato("contato@twitter.com", Tipo.TWITTER);
		user.adicionarContato(contato);

		Contato contatoRecuperado = user.getContatoPorTipo(Tipo.TWITTER);
		assertNotNull(contatoRecuperado);
	}

	@Test
	public void recuperarDoisContatosDoUsuarioPorTipoDeContato() {
		Contato contato = new Contato("contato@twitter.com", Tipo.TWITTER);
		user.adicionarContato(contato);
		Contato contato1Recuperado = user.getContatoPorTipo(Tipo.TWITTER);
		assertEquals("contato@twitter.com", contato1Recuperado.getEndereco());

		Contato contato2 = new Contato("contato@facebook.com", Tipo.FACEBOOK);
		user.adicionarContato(contato2);
		Contato contato2Recuperado = user.getContatoPorTipo(Tipo.FACEBOOK);
		assertEquals("contato@facebook.com", contato2Recuperado.getEndereco());
	}

	@Test
	public void gravaRecuperaUsuarioNoBanco() {
		usuarioService.adicionar(user);
		Usuario recuperado = usuarioService.buscarUsuarioPorEmail(user.getEmail());
		assertEquals("Anderson", recuperado.getNome());
		assertEquals("email@contato.com", recuperado.getEmail());
	}

	@Test(expected = IllegalArgumentException.class)
	public void lancaExcecaoAoBuscarUsuarioComEmailErrado() {
		usuarioService.buscarUsuarioPorEmail("email@inexistente.com");
	}

	@Test
	public void gravaRecuperaERemoverUsuarioComContatoNoBanco() {
		Usuario user = new Usuario("Pedro", "321", "pedro@contato.com");
		user.adicionarContato(new Contato("email@facebook.com", Tipo.FACEBOOK));
		user.adicionarContato(new Contato("email@twitter.com", Tipo.TWITTER));
		usuarioService.adicionar(user);
		Usuario usuarioRecuperado = usuarioService.buscarUsuarioPorEmail("pedro@contato.com");

		assertEquals("Pedro", usuarioRecuperado.getNome());
		assertEquals("321", usuarioRecuperado.getSenha());

		assertEquals("email@facebook.com", usuarioRecuperado.getContatoPorTipo(Tipo.FACEBOOK).getEndereco());
		assertEquals("email@twitter.com", usuarioRecuperado.getContatoPorTipo(Tipo.TWITTER).getEndereco());
		usuarioService.removerPorId(usuarioRecuperado.getId());
	}
}
