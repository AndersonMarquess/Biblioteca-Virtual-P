package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import com.andersonmarques.bvp.dto.UsuarioDTO;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.CredenciaisLogin;
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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate clienteTeste;
	private HttpHeaders headers = new HttpHeaders();
	private static final String ADMIN_PASSWORD = "password";
	private static final String ADMIN_EMAIL = "admin@email.com";
	private static final String USUARIO_PASSWORD = "123";
	private static final String USUARIO_EMAIL = "necronomicon@email.com";
	private static final String AUTHORIZATION = "Authorization";

	@Before
	public void prepararObjetos() {
		headers.add("content-type", "application/json");
		headers.add("accept", "*/*");
	}

	private void setTokenHeaderParaUsuario(Usuario usuario) {
		headers.set(AUTHORIZATION, getTokenParaCredenciais(new CredenciaisLogin(usuario.getEmail(), "123")));
	}

	private String getTokenParaCredenciais(CredenciaisLogin credenciaisLogin) {
		ResponseEntity<String> postForEntity = clienteTeste.postForEntity("/login", credenciaisLogin, String.class);
		String token = postForEntity.getHeaders().get("Bearer").get(0);
		return "Bearer " + token;
	}

	private Usuario getUsuarioAleatorio() {
		String stringAleatoria = UUID.randomUUID().toString();
		Usuario usuario = new Usuario(stringAleatoria, "123", stringAleatoria + "@email.com");
		usuario.adicionarContato(new Contato(stringAleatoria, Tipo.TWITTER));
		return usuario;
	}

	private ResponseEntity<List<UsuarioDTO>> listarTodosOsUsuarios() {
		return clienteTeste.exchange("/v1/usuario/all", HttpMethod.GET, new HttpEntity<>(headers),
				getTipoListaDeUsuario());
	}

	/**
	 * Retorna o tipo List<UsuarioDTO>.
	 *
	 * @return List<UsuarioDTO>
	 */
	private ParameterizedTypeReference<List<UsuarioDTO>> getTipoListaDeUsuario() {
		return new ParameterizedTypeReference<List<UsuarioDTO>>() {
		};
	}

	private ResponseEntity<Usuario> postUsuario(Usuario usuario) {
		return clienteTeste.postForEntity("/v1/usuario", new HttpEntity<>(usuario, headers), Usuario.class);
	}

	private ResponseEntity<Usuario> buscarUsuarioPorId(Usuario usuario) {
		return clienteTeste.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.GET, new HttpEntity<>(headers),
				Usuario.class);
	}

	private ResponseEntity<String> deletarUsuario(Usuario usuario) {
		return clienteTeste.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, new HttpEntity<>(headers),
				String.class);
	}

	private ResponseEntity<Usuario> atualizarUsuario(Usuario usuarioAtualizado) {
		ResponseEntity<Usuario> respostaPUT = clienteTeste.exchange("/v1/usuario", HttpMethod.PUT,
				new HttpEntity<>(usuarioAtualizado, headers), Usuario.class);
		return respostaPUT;
	}

	private Usuario postUsuarioAleatorio() {
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> respostaPOST = postUsuario(usuario);
		assertEquals(200, respostaPOST.getStatusCodeValue());
		return usuario;
	}

	@Test
	public void autenticacaoSemRoleAdminParaListarUsuariosRecebe_StatusCode403() {
		headers.set(AUTHORIZATION, getTokenParaCredenciais(new CredenciaisLogin(USUARIO_EMAIL, USUARIO_PASSWORD)));
		ResponseEntity<String> resposta = clienteTeste.exchange("/v1/usuario/all", HttpMethod.GET,
				new HttpEntity<>(headers), String.class);

		assertEquals(403, resposta.getStatusCodeValue());
	}

	@Test
	public void autenticacaoComRoleAdminParaListarUsuariosRecebe_StatusCode200() {
		headers.set(AUTHORIZATION, getTokenParaCredenciais(new CredenciaisLogin(ADMIN_EMAIL, ADMIN_PASSWORD)));
		ResponseEntity<List<UsuarioDTO>> resposta = listarTodosOsUsuarios();
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void buscarDetalhesDeSiPorIdRecebe_StatusCode200() {
		/* Criar o usuário */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<Usuario> resposta = buscarUsuarioPorId(usuario);

		assertNotNull(resposta.getBody());
		assertEquals(usuario.getNome(), resposta.getBody().getNome());
		assertTrue(usuario.getContatos().containsAll(resposta.getBody().getContatos()));
		assertEquals(200, resposta.getStatusCodeValue());

		/* Remover usuário */
		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioBuscarDetalhesDeOutroUsuarioRecebe_StatusCode401() {
		/* Criar o usuários */
		Usuario usuario = postUsuarioAleatorio();
		Usuario usuario2 = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);

		/* Buscar detalhes */
		ResponseEntity<Usuario> resposta = buscarUsuarioPorId(usuario2);
		assertNotNull(resposta.getBody());
		assertEquals(401, resposta.getStatusCodeValue());

		/* Remover usuários */
		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());

		setTokenHeaderParaUsuario(usuario2);
		ResponseEntity<String> resposta2DELETE = deletarUsuario(usuario2);
		assertEquals(200, resposta2DELETE.getStatusCodeValue());
	}

	@Test
	public void verificarPermissoesDoUsuarioPorId() {
		/* Criar */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);

		ResponseEntity<Usuario> resposta = buscarUsuarioPorId(usuario);
		assertNotNull(resposta.getBody());
		assertEquals(200, resposta.getStatusCodeValue());
		assertFalse(resposta.getBody().getPermissoes().isEmpty());
		assertTrue(resposta.getBody().getPermissoes().stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));

		/* Remover */
		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioRemoverSiMesmoRecebe_StatusCode200() {
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioAtualizarSiMesmoRecebe_StatusCode200() {
		/* Criar */
		Usuario usuario = getUsuarioAleatorio();
		usuario.adicionarContato(new Contato("mock_junit@fb.com", Tipo.FACEBOOK));

		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertNotNull(usuarioPOST.getBody());
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		/* Atualizar */
		Usuario usuarioAtualizado = usuarioPOST.getBody();
		usuarioAtualizado.setNome("Nome atualizado");
		usuarioAtualizado.setEmail("email@atualizado.com");
		usuarioAtualizado.getContatoPorTipo(Tipo.FACEBOOK).setEndereco("atualizado@facebook.com");

		setTokenHeaderParaUsuario(usuario);
		ResponseEntity<Usuario> respostaPUT = atualizarUsuario(usuarioAtualizado);
		assertNotNull(respostaPUT.getBody());
		assertEquals(200, respostaPUT.getStatusCodeValue());
		assertEquals("email@atualizado.com", respostaPUT.getBody().getEmail());
		assertEquals("Nome atualizado", respostaPUT.getBody().getNome());

		/* Remover */
		setTokenHeaderParaUsuario(respostaPUT.getBody());
		ResponseEntity<String> respostaDELETE = deletarUsuario(respostaPUT.getBody());
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void UsarBCryptParaAtualizarSenhaDoUsuarioRecebe_StatusCode200() {
		/* Criar */
		Usuario usuario = postUsuarioAleatorio();

		setTokenHeaderParaUsuario(usuario);

		/* Atualizar */
		usuario.setNome("SENHA abc");
		usuario.setSenha("abc");
		assertTrue(BCrypt.checkpw("abc", usuario.getSenha()));
		assertEquals(usuario.getSenha().length(), usuario.getSenha().length());

		ResponseEntity<Usuario> respostaPUT = atualizarUsuario(usuario);
		assertNotNull(respostaPUT.getBody());
		assertEquals(200, respostaPUT.getStatusCodeValue());
		assertTrue(BCrypt.checkpw("abc", respostaPUT.getBody().getSenha()));

		/* Remover */
		ResponseEntity<String> respostaDELETE = deletarUsuario(respostaPUT.getBody());
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}
}
