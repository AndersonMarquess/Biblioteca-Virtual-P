package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;
import com.andersonmarques.bvp.service.PermissaoService;
import com.andersonmarques.bvp.service.UsuarioAutenticavelService;
import com.andersonmarques.bvp.service.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {

	private Usuario user;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PermissaoService permissaoService;
	@Autowired
	private UsuarioAutenticavelService usuarioAutenticavelService;

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
		assertTrue(new BCryptPasswordEncoder().matches("321", usuarioRecuperado.getSenha()));

		assertEquals("email@facebook.com", usuarioRecuperado.getContatoPorTipo(Tipo.FACEBOOK).getEndereco());
		assertEquals("email@twitter.com", usuarioRecuperado.getContatoPorTipo(Tipo.TWITTER).getEndereco());
		usuarioService.removerPorId(usuarioRecuperado.getId());
	}

	@Test
	public void gravarERecuperarPermissoesDosUsuariosNoBanco() {
		Usuario user = new Usuario("Pedro", "321", "pedro@contato.com");
		Usuario admin = new Usuario("Gabriel", "231", "gabriel@contato.com");
		Usuario adminMaster = new Usuario("Vanessa", "333", "vanessa@contato.com");
		user.adicionarPermissao(new Permissao("ROLE_USER"));
		admin.adicionarPermissao(new Permissao("ADMIN"), new Permissao("ROLE_USER"));
		adminMaster.adicionarPermissao(new Permissao("ADMIN"), new Permissao("ROLE_USER"), new Permissao("MASTER"));

		Usuario userRecuperado = usuarioService.adicionar(user);
		Usuario adminRecuperado = usuarioService.adicionar(admin);
		Usuario adminMasterRecuperado = usuarioService.adicionar(adminMaster);

		List<Permissao> userPermissoesRecuperada = permissaoService
				.buscarPermissoesPorIdUsuario(userRecuperado.getId());
		assertTrue(userPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));

		List<Permissao> adminPermissoesRecuperada = permissaoService
				.buscarPermissoesPorIdUsuario(adminRecuperado.getId());
		assertTrue(adminPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));
		assertTrue(adminPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_ADMIN")));

		List<Permissao> adminMasterPermissoesRecuperada = permissaoService
				.buscarPermissoesPorIdUsuario(adminMasterRecuperado.getId());
		assertTrue(adminMasterPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));
		assertTrue(adminMasterPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_ADMIN")));
		assertTrue(adminMasterPermissoesRecuperada.stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_MASTER")));

		usuarioService.removerPorId(userRecuperado.getId());
	}

	@Test
	public void gravarUsuarioComSenhaCriptografadaNoBanco() {
		Usuario userRecuperado = usuarioService.adicionar(new Usuario("Zezinho", "123", "zezem@email.com"));

		assertTrue(new BCryptPasswordEncoder().matches("123", userRecuperado.getSenha()));
	}
	
	@Test
	public void buscarUserDetailsPorNome() {
		UserDetails userDetails = usuarioAutenticavelService.loadUserByUsername("necronomicon");
		
		assertTrue(new BCryptPasswordEncoder().matches("123", userDetails.getPassword()));
		assertTrue(userDetails.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_USER")));
		assertTrue(userDetails.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_MASTER")));
	}
}
