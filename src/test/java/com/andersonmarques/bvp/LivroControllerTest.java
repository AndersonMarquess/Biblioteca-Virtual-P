package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Usuario;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LivroControllerTest {

	private static final String USER_PASSWORD = "123";
	private static final String USER_EMAIL = "necronomicon@email.com";
	private HttpHeaders headers = new HttpHeaders();

	@Autowired
	private TestRestTemplate clienteTest;

	@Before
	public void prepararObjetos() {
		headers.add("content-type", "application/json");
		headers.add("accept", "*/*");
	}

	@Test
	public void listarTodosOsLivrosSemRoleUserRecebe_StatusCode401() {
		ResponseEntity<String> resposta = clienteTest.getForEntity("/v1/livro/all", String.class);
		assertEquals(401, resposta.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosComRoleUserRecebe_StatusCode200() {
		ResponseEntity<String> resposta = clienteTest.withBasicAuth(USER_EMAIL, USER_PASSWORD)
				.getForEntity("/v1/livro/all", String.class);
		assertEquals(200, resposta.getStatusCodeValue());
	}

	private ParameterizedTypeReference<List<Livro>> getTipoListaDeLivros() {
		return new ParameterizedTypeReference<List<Livro>>() {
		};
	}

	@Test
	public void mapearLivrosRecuperados() {
		ResponseEntity<List<Livro>> respostaGET = clienteTest.withBasicAuth(USER_EMAIL, USER_PASSWORD)
				.exchange("/v1/livro/all", HttpMethod.GET, null, getTipoListaDeLivros());

		assertNotNull(respostaGET.getBody());
		assertEquals(200, respostaGET.getStatusCodeValue());
	}

	@Test
	public void cadastrarLivroComRoleUserRecebe_StatusCode200() {
		Usuario usuario = new Usuario("Usuario mock 2019", "123", "user_mock@email.com");
		ResponseEntity<Usuario> usuarioPostResposta = clienteTest.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);
		assertNotNull(usuarioPostResposta.getBody());
		assertEquals(200, usuarioPostResposta.getStatusCodeValue());

		Livro livro = new Livro("978-85-83b8-bb9-4", "Sandman - Prelúdio 3", "Lindo e confuso",
				"https://img.assinaja.com/assets/tZ/004/img/81104_520x520.jpg", usuarioPostResposta.getBody().getId());
		livro.adicionarCategoria(new Categoria("Fantasia"));

		ResponseEntity<Livro> livroPostResposta = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro", HttpMethod.POST, new HttpEntity<>(livro, headers), Livro.class);
		assertEquals(200, livroPostResposta.getStatusCodeValue());
		assertNotNull(livroPostResposta.getBody());

		ResponseEntity<String> respostaDELETE = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosDoUsuarioPorIdComRoleUserRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = new Usuario("u", "1", "u@1.com");
		ResponseEntity<Usuario> usuarioPOST = clienteTest.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = new Livro("4", "S", "L", "url", usuarioPOST.getBody().getId());
		livro.adicionarCategoria(new Categoria("F"));

		ResponseEntity<Livro> livroPOST = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro", HttpMethod.POST, new HttpEntity<>(livro, headers), Livro.class);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Listar livros do usuário */
		ResponseEntity<List<Livro>> livrosUsuario = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro/all/" + usuario.getId(), HttpMethod.GET, null, getTipoListaDeLivros());
		assertNotNull(livrosUsuario.getBody());
		assertEquals(1, livrosUsuario.getBody().size());

		/* Remover criações */
		ResponseEntity<String> usuarioDELETE = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverLivroDeOutroUsuarioRecebe_StatusCode401() {
		/* Criação dos mocks */
		Usuario usuario = new Usuario("u", "1", "u@1.com");
		ResponseEntity<Usuario> usuarioPOST = clienteTest.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = new Livro("4", "S", "L", "url", usuarioPOST.getBody().getId());
		livro.adicionarCategoria(new Categoria("Q"));

		ResponseEntity<Livro> livroPOST = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro", HttpMethod.POST, new HttpEntity<>(livro, headers), Livro.class);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = clienteTest.withBasicAuth(USER_EMAIL, USER_PASSWORD)
				.exchange("/v1/livro/" + livroPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(401, livroDELETE.getStatusCodeValue());

		ResponseEntity<String> usuarioDELETE = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverSeuLivroRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = new Usuario("u", "1", "u@1.com");
		ResponseEntity<Usuario> usuarioPOST = clienteTest.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = new Livro("4", "S", "L", "url", usuarioPOST.getBody().getId());
		livro.adicionarCategoria(new Categoria("Q"));

		ResponseEntity<Livro> livroPOST = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro", HttpMethod.POST, new HttpEntity<>(livro, headers), Livro.class);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro/" + livroPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(200, livroDELETE.getStatusCodeValue());

		ResponseEntity<String> usuarioDELETE = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}
}
