package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.CredenciaisLogin;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;

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

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LivroControllerTest {

	@Autowired
	private TestRestTemplate clienteTeste;
	private HttpHeaders headers = new HttpHeaders();
	private static final String USUARIO_PASSWORD = "123";
	private static final String USUARIO_EMAIL = "necronomicon@email.com";
	private static final String AUTHORIZATION = "Authorization";

	@Before
	public void prepararObjetos() {
		headers.add("content-type", "application/json");
		headers.add("accept", "*/*");
	}

	private Usuario postUsuarioAleatorio() {
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> respostaPOST = postUsuario(usuario);
		assertEquals(200, respostaPOST.getStatusCodeValue());
		return usuario;
	}

	private String getTokenUsuario() {
		return getTokenParaCredenciais(new CredenciaisLogin(USUARIO_EMAIL, USUARIO_PASSWORD));
	}

	private void setTokenHeaderParaUsuario(Usuario usuario) {
		headers.set(AUTHORIZATION, getTokenParaCredenciais(new CredenciaisLogin(usuario.getEmail(), "123")));
	}

	private String getTokenParaCredenciais(CredenciaisLogin credenciaisLogin) {
		ResponseEntity<String> postForEntity = clienteTeste.postForEntity("/login", credenciaisLogin, String.class);
		String token = postForEntity.getHeaders().get("Bearer").get(0);
		return "Bearer " + token;
	}

	private String getStringAleatoria() {
		return UUID.randomUUID().toString();
	}

	private Usuario getUsuarioAleatorio() {
		Usuario usuario = new Usuario(getStringAleatoria(), getStringAleatoria(), getStringAleatoria() + "@email.com");
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

	private ResponseEntity<List<Livro>> buscarTodosOsLivros() {
		return clienteTeste.exchange("/v1/livro/all", HttpMethod.GET, new HttpEntity<>(headers),
				getTipoListaDeLivros());
	}

	private ResponseEntity<List<Livro>> listarTodosOsLivrosDoUsuario(Usuario usuario) {
		return clienteTeste.exchange("/v1/livro/all/" + usuario.getId(), HttpMethod.GET, new HttpEntity<>(headers),
				getTipoListaDeLivros());
	}

	private ResponseEntity<Livro> postLivro(Livro livro) {
		return clienteTeste.postForEntity("/v1/livro", new HttpEntity<>(livro, headers), Livro.class);
	}

	private ResponseEntity<Livro> atualizarLivro(Livro livro) {
		return clienteTeste.exchange("/v1/livro", HttpMethod.PUT, new HttpEntity<>(livro, headers), Livro.class);
	}

	private ResponseEntity<String> deletarLivro(String idLivro) {
		return clienteTeste.exchange("/v1/livro/" + idLivro, HttpMethod.DELETE, new HttpEntity<>(headers),
				String.class);
	}

	private ResponseEntity<Usuario> postUsuario(Usuario usuario) {
		return clienteTeste.postForEntity("/v1/usuario", new HttpEntity<>(usuario, headers), Usuario.class);
	}

	private ResponseEntity<String> deletarUsuario(Usuario usuario) {
		return clienteTeste.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, new HttpEntity<>(headers),
				String.class);
	}

	@Test
	public void listarTodosOsLivrosSemRoleUserRecebe_StatusCode403() {
		ResponseEntity<String> resposta = clienteTeste.getForEntity("/v1/livro/all", String.class);
		assertEquals(403, resposta.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosComRoleUserRecebe_StatusCode200() {
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<List<Livro>> resposta = buscarTodosOsLivros();
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void cadastrarLivroComRoleUserRecebe_StatusCode200() {
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPostResposta = postLivro(livro);
		assertEquals(200, livroPostResposta.getStatusCodeValue());
		assertNotNull(livroPostResposta.getBody());

		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosDoUsuarioPorIdComRoleUserRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Listar livros do usuário */
		ResponseEntity<List<Livro>> livrosUsuario = listarTodosOsLivrosDoUsuario(usuario);
		assertNotNull(livrosUsuario.getBody());
		assertEquals(1, livrosUsuario.getBody().size());

		/* Remover criações */
		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverLivroDeOutroUsuarioRecebe_StatusCode401() {
		/* Criação dos mocks */
		Usuario usuario = postUsuarioAleatorio();
		headers.set(AUTHORIZATION, getTokenUsuario());
		
		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = deletarLivro(livroPOST.getBody().getId());
		assertEquals(401, livroDELETE.getStatusCodeValue());

		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverSeuLivroRecebe_StatusCode200() {
		/* Criação dos mocks */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> livroDELETE = clienteTeste.exchange("/v1/livro/" + livroPOST.getBody().getId(),
				HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

		assertEquals(200, livroDELETE.getStatusCodeValue());

		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}

	@Test
	public void apagarUsuarioTambemRemoveSeusLivros() {
		/* Criação dos mocks */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		/* Remover criações */
		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());

		headers.set(AUTHORIZATION, getTokenUsuario());
		ResponseEntity<List<Livro>> respostaLivrosDoUsuario = clienteTeste.exchange("/v1/livro/all/" + usuario.getId(),
				HttpMethod.GET, new HttpEntity<>(headers), getTipoListaDeLivros());
		assertTrue(respostaLivrosDoUsuario.getBody().isEmpty());
		assertEquals(200, respostaLivrosDoUsuario.getStatusCodeValue());
	}

	@Test
	public void usuarioAtualizarDadosDoSeuLivroRecebe_StatusCode200() {
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);

		Livro livro = getLivroAleatorioComIdDono(usuario.getId());
		ResponseEntity<Livro> livroPOST = postLivro(livro);
		assertEquals(200, livroPOST.getStatusCodeValue());

		livro = livroPOST.getBody();
		livro.setDescricao("Desc atualizado");
		livro.setTitulo("Atualização");
		livro.getCategorias().get(0).setNome("de X para QUE");
		livro.adicionarCategoria(new Categoria("Nova categoria pós atualização"));

		ResponseEntity<Livro> livroPUT = atualizarLivro(livro);
		assertEquals(200, livroPUT.getStatusCodeValue());

		ResponseEntity<List<Livro>> respostaLivrosDoUsuario = listarTodosOsLivrosDoUsuario(usuario);
		assertNotNull(respostaLivrosDoUsuario.getBody());

		Livro livroAposUpdate = respostaLivrosDoUsuario.getBody().get(0);
		assertEquals(livro.getId(), livroAposUpdate.getId());
		assertEquals(livro.getDescricao(), livroAposUpdate.getDescricao());
		assertTrue(livroAposUpdate.getCategorias().stream().anyMatch(c -> c.getNome().equals("de X para QUE")));
		assertTrue(livroAposUpdate.getCategorias().stream()
				.anyMatch(c -> c.getNome().equals("Nova categoria pós atualização")));

		ResponseEntity<String> usuarioDELETE = deletarUsuario(usuario);
		assertEquals(200, usuarioDELETE.getStatusCodeValue());
	}
}
