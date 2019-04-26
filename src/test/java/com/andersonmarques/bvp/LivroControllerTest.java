package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

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
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;

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

	private String getStringAleatoria() {
		return UUID.randomUUID().toString();
	}

	private Usuario getUsuarioAleatorio() {
		Usuario usuario = new Usuario(getStringAleatoria(), getStringAleatoria(), getStringAleatoria());
		usuario.adicionarContato(new Contato(getStringAleatoria(), Tipo.TWITTER));
		usuario.setSenha("123");
		return usuario;
	}

	private Livro getLivroAleatorioComIdDono(String idDonoLivro) {
		Livro livro = new Livro(getStringAleatoria(), getStringAleatoria(), getStringAleatoria(), getStringAleatoria(),
				idDonoLivro);
		livro.adicionarCategoria(new Categoria(getStringAleatoria()));
		return livro;
	}

	private ParameterizedTypeReference<List<Livro>> getTipoListaDeLivros() {
		return new ParameterizedTypeReference<List<Livro>>() {
		};
	}

	private ResponseEntity<Livro> postLivro(String email, String senha, Livro livro) {
		return clienteTest.withBasicAuth(email, senha).postForEntity("/v1/livro", new HttpEntity<>(livro, headers),
				Livro.class);
	}

	private ResponseEntity<Usuario> postUsuario(Usuario usuario) {
		return clienteTest.postForEntity("/v1/usuario", new HttpEntity<>(usuario, headers), Usuario.class);
	}

	private ResponseEntity<String> deletarUsuario(String email, String senha, Usuario usuario) {
		return clienteTest.withBasicAuth(email, senha).exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE,
				null, String.class);
	}

	private ResponseEntity<List<Livro>> buscarTodosOsLivros(String email, String senha) {
		return clienteTest.withBasicAuth(email, senha).exchange("/v1/livro/all", HttpMethod.GET, null,
				getTipoListaDeLivros());
	}

	private ResponseEntity<List<Livro>> listarTodosOsLivrosDoUsuario(String email, String senha, Usuario usuario) {
		return clienteTest.withBasicAuth(email, senha).exchange("/v1/livro/all/" + usuario.getId(), HttpMethod.GET,
				null, getTipoListaDeLivros());
	}

	@Test
	public void listarTodosOsLivrosSemRoleUserRecebe_StatusCode401() {
		ResponseEntity<String> resposta = clienteTest.getForEntity("/v1/livro/all", String.class);
		assertEquals(401, resposta.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosComRoleUserRecebe_StatusCode200() {
		ResponseEntity<List<Livro>> resposta = buscarTodosOsLivros(USER_EMAIL, USER_PASSWORD);
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void cadastrarLivroComRoleUserRecebe_StatusCode200() {
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPostResposta = postUsuario(usuario);
		assertNotNull(usuarioPostResposta.getBody());
		assertEquals(200, usuarioPostResposta.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPostResposta = postLivro(usuario.getEmail(), "123", livro);
		assertEquals(200, livroPostResposta.getStatusCodeValue());
		assertNotNull(livroPostResposta.getBody());

		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosDoUsuarioPorIdComRoleUserRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(usuario.getEmail(), "123",  livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Listar livros do usuário */
		ResponseEntity<List<Livro>> livrosUsuario = listarTodosOsLivrosDoUsuario(usuario.getEmail(), "123", usuario);
		assertNotNull(livrosUsuario.getBody());
		assertEquals(1, livrosUsuario.getBody().size());

		/* Remover criações */
		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverLivroDeOutroUsuarioRecebe_StatusCode401() {
		/* Criação dos mocks */
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(usuario.getEmail(), "123", livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = clienteTest.withBasicAuth(USER_EMAIL, USER_PASSWORD)
				.exchange("/v1/livro/" + livroPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(401, livroDELETE.getStatusCodeValue());

		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverSeuLivroRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(usuario.getEmail(), "123", livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = clienteTest.withBasicAuth(usuario.getEmail(), "123")
				.exchange("/v1/livro/" + livroPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(200, livroDELETE.getStatusCodeValue());

		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void apagarUsuarioTambemRemoveSeusLivros() {
		/* Criação dos mocks */
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(usuario.getEmail(), "123", livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());

		ResponseEntity<List<Livro>> respostaLivrosDoUsuario = clienteTest.withBasicAuth("admin@email.com", "password")
				.exchange("/v1/livro/all/" + usuario.getId(), HttpMethod.GET, null, getTipoListaDeLivros());
		assertEquals(0, respostaLivrosDoUsuario.getBody().size());
		assertEquals(200, respostaLivrosDoUsuario.getStatusCodeValue());
	}

	@Test
	public void usuarioAtualizarDadosDoSeuLivroRecebe_StatusCode200() {
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(usuario.getEmail(), "123", livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		livro = livroPOST.getBody();
		livro.setDescricao("Desc atualizado");
		livro.setTitulo("Atualização");
		livro.getCategorias().get(0).setNome("de X para QUE");
		livro.adicionarCategoria(new Categoria("Nova categoria pós atualização"));

		ResponseEntity<Livro> livroPUT = clienteTest.withBasicAuth(usuario.getEmail(), "123")
				.exchange("/v1/livro", HttpMethod.PUT, new HttpEntity<>(livro, headers), Livro.class);
		assertEquals(200, livroPUT.getStatusCodeValue());

		ResponseEntity<List<Livro>> respostaLivrosDoUsuario = listarTodosOsLivrosDoUsuario(usuario.getEmail(), "123", usuario);
		assertNotNull(respostaLivrosDoUsuario.getBody());

		Livro livroAposUpdate = respostaLivrosDoUsuario.getBody().get(0);
		assertEquals(livro.getId(), livroAposUpdate.getId());
		assertEquals(livro.getDescricao(), livroAposUpdate.getDescricao());
		assertTrue(livroAposUpdate.getCategorias().stream().anyMatch(c -> c.getNome().equals("de X para QUE")));
		assertTrue(livroAposUpdate.getCategorias().stream()
				.anyMatch(c -> c.getNome().equals("Nova categoria pós atualização")));

		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario.getEmail(), "123", usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}
}
