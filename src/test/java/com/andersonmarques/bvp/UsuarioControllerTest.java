package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import com.andersonmarques.bvp.dto.UsuarioDTO;
import com.andersonmarques.bvp.model.Contato;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.model.enums.Tipo;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate clienteTeste;
	private HttpHeaders headers = new HttpHeaders();
	private static final String ADMIN_PASSWORD = "password";
	private static final String ADMIN_EMAIL = "admin@email.com";

	@Before
	public void prepararObjetos() {
		headers.add("content-type", "application/json");
		headers.add("accept", "*/*");
	}

	@Test
	public void autenticacaoComRoleUserParaListarUsuariosRecebe_StatusCode403() {
		ResponseEntity<String> resposta = clienteTeste.withBasicAuth("necronomicon@email.com", "123")
				.getForEntity("/v1/usuario/all", String.class);

		assertEquals(403, resposta.getStatusCodeValue());
	}

	@Test
	public void autenticacaoComRoleAdminParaListarUsuariosRecebe_StatusCode200() {
		ResponseEntity<String> resposta = clienteTeste.withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
				.getForEntity("/v1/usuario/all", String.class);

		assertEquals(200, resposta.getStatusCodeValue());
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

	@Test
	public void buscarDetalhesDeSiPorIdRecebe_StatusCode200() {
		/* Criar o usuário */
		Usuario usuario = new Usuario("usuario ver detalhes de si", "senha", "emial@ver_detalhes_de_si.com");
		HttpEntity<Usuario> usuarioEntity_POST = new HttpEntity<>(usuario, headers);
		usuario.adicionarContato(new Contato("@usuário ver detalhes de si", Tipo.TWITTER));

		ResponseEntity<Usuario> respostaPOST = clienteTeste.exchange("/v1/usuario", HttpMethod.POST, usuarioEntity_POST,
				Usuario.class);
		assertEquals(200, respostaPOST.getStatusCodeValue());

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);

		assertNotNull(resposta.getBody());
		assertEquals(usuario.getNome(), resposta.getBody().getNome());
		assertTrue(usuario.getContatos().containsAll(resposta.getBody().getContatos()));
		assertEquals(200, resposta.getStatusCodeValue());

		/* Remover usuário */
		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioBuscarDetalhesDeOutroUsuarioRecebe_StatusCode401() {
		/* Criar o usuários */
		Usuario usuario = new Usuario("usuario 1", "senha", "emial1@ver_detalhes_de_outro_da_erro.com");
		usuario.adicionarContato(new Contato("@usuário ver detalhes de outro", Tipo.TWITTER));
		Usuario usuario2 = new Usuario("usuario 2", "senha", "emial@contato-user2.com");
		usuario.adicionarContato(new Contato("@usuário", Tipo.TWITTER));

		ResponseEntity<Usuario> respostaPOST = clienteTeste.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);
		assertEquals(200, respostaPOST.getStatusCodeValue());

		respostaPOST = clienteTeste.exchange("/v1/usuario", HttpMethod.POST, new HttpEntity<>(usuario2, headers),
				Usuario.class);
		assertEquals(200, respostaPOST.getStatusCodeValue());

		/* Buscar detalhes */
		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.getForEntity("/v1/usuario/" + usuario2.getId(), Usuario.class);
		assertNotNull(resposta.getBody());
		assertEquals(401, resposta.getStatusCodeValue());

		/* Remover usuário */
		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());

		respostaDELETE = clienteTeste.withBasicAuth(usuario2.getEmail(), usuario2.getSenha())
				.exchange("/v1/usuario/" + usuario2.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void verificarPermissoesDoUsuarioPorId() {
		ResponseEntity<List<UsuarioDTO>> usuarios = clienteTeste.withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());
		String idUsuario = usuarios.getBody().get(0).getId();

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth(ADMIN_EMAIL, ADMIN_PASSWORD)
				.exchange("/v1/usuario/" + idUsuario, HttpMethod.GET, null, Usuario.class);

		assertNotNull(resposta.getBody());
		assertEquals(200, resposta.getStatusCodeValue());
		assertFalse(resposta.getBody().getPermissoes().isEmpty());
		assertTrue(resposta.getBody().getPermissoes().stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));
	}

	@Test
	public void usuarioRemoverSiMesmoRecebe_StatusCode200() {
		Usuario usuario = new Usuario("mock", "senha", "email@mock.com");
		HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, headers);

		ResponseEntity<Usuario> respostaPOST = clienteTeste.exchange("/v1/usuario", HttpMethod.POST, usuarioEntity,
				Usuario.class);
		assertNotNull(respostaPOST.getBody());
		assertEquals(200, respostaPOST.getStatusCodeValue());

		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + respostaPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioAtualizarSiMesmoRecebe_StatusCode200() {
		Usuario usuario = new Usuario("mock", "senha", "email@mock.com");
		usuario.adicionarContato(new Contato("@mock_junit", Tipo.TWITTER));
		usuario.adicionarContato(new Contato("mock_junit@fb.com", Tipo.FACEBOOK));

		ResponseEntity<Usuario> respostaPOST = clienteTeste.exchange("/v1/usuario", HttpMethod.POST,
				new HttpEntity<>(usuario, headers), Usuario.class);

		assertNotNull(respostaPOST.getBody());
		assertEquals(200, respostaPOST.getStatusCodeValue());

		Usuario usuarioPOST = respostaPOST.getBody();
		usuarioPOST.setEmail("mock_email_junit@email.com");
		usuarioPOST.getContatoPorTipo(Tipo.FACEBOOK).setEndereco("mock_junit@facebook.com");

		HttpEntity<Usuario> usuarioEntity_PUT = new HttpEntity<>(usuarioPOST, headers);

		ResponseEntity<Usuario> respostaPUT = clienteTeste.withBasicAuth("email@mock.com", "senha")
				.exchange("/v1/usuario", HttpMethod.PUT, usuarioEntity_PUT, Usuario.class);

		assertNotNull(respostaPUT.getBody());
		assertEquals(200, respostaPUT.getStatusCodeValue());
		assertEquals("mock_junit@facebook.com", respostaPUT.getBody().getContatoPorTipo(Tipo.FACEBOOK).getEndereco());
		assertEquals("mock_email_junit@email.com", respostaPUT.getBody().getEmail());

		ResponseEntity<String> respostaDELETE = clienteTeste
				.withBasicAuth("mock_email_junit@email.com", usuario.getSenha())
				.exchange("/v1/usuario/" + respostaPUT.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}
}
