package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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

	@Test
	public void autenticacaoComRoleUserParaListarUsuariosRecebe_StatusCode200() {
		ResponseEntity<String> resposta = clienteTeste.withBasicAuth("necronomicon", "123")
				.getForEntity("/v1/usuario/all", String.class);

		System.out.println(resposta.getBody());

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
	public void buscarDetalhesDoUsuarioPorIdComRoleAdminRecebe_StatusCode200() {
		ResponseEntity<List<UsuarioDTO>> usuarios = clienteTeste.withBasicAuth("necronomicon", "123")
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());

		UsuarioDTO usuario = usuarios.getBody().get(0);

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth("admin", "password")
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);

		assertNotNull(resposta.getBody());
		assertEquals(usuario.getNome(), resposta.getBody().getNome());
		assertTrue(usuario.getContatos().containsAll(resposta.getBody().getContatos()));
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void buscarDetalhesDoUsuarioPorIdComRoleUserRecebe_StatusCode403() {
		ResponseEntity<List<UsuarioDTO>> usuarios = clienteTeste.withBasicAuth("necronomicon", "123")
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());

		UsuarioDTO usuario = usuarios.getBody().get(0);

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth("necronomicon", "123")
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);

		assertEquals(403, resposta.getStatusCodeValue());
	}

	@Test
	public void verificarPermissoesDoUsuarioPorId() {
		ResponseEntity<List<UsuarioDTO>> usuarios = clienteTeste.withBasicAuth("necronomicon", "123")
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());
		String idUsuario = usuarios.getBody().get(0).getId();

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth("admin", "password")
				.exchange("/v1/usuario/" + idUsuario, HttpMethod.GET, null, Usuario.class);

		assertNotNull(resposta.getBody());
		assertEquals(200, resposta.getStatusCodeValue());
		assertFalse(resposta.getBody().getPermissoes().isEmpty());
		assertTrue(resposta.getBody().getPermissoes().stream().anyMatch(p -> p.getNomePermissao().equals("ROLE_USER")));
	}

	@Test
	public void removerUsuarioPorIdAutenticadoComRoleAdminRecebe_StatusCode200() {
		Usuario usuario = new Usuario("mock", "senha", "email@mock.com");
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, headers);

		ResponseEntity<Usuario> respostaPOST = clienteTeste.withBasicAuth("admin", "password").exchange("/v1/usuario",
				HttpMethod.POST, usuarioEntity, Usuario.class);

		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth("admin", "password")
				.exchange("/v1/usuario/" + respostaPOST.getBody().getId(), HttpMethod.DELETE, null, String.class);

		System.out.println(respostaDELETE.getBody());
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void atualizarUsuarioAutenticadoComRoleAdminRecebe_StatusCode200() {
		Usuario usuario = new Usuario("mock", "senha", "email@mock.com");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Usuario> usuarioEntity_POST = new HttpEntity<>(usuario, headers);

		headers.add("content-type", "application/json");
		usuario.adicionarContato(new Contato("@mock_junit", Tipo.TWITTER));
		usuario.adicionarContato(new Contato("mock_junit@fb.com", Tipo.FACEBOOK));

		ResponseEntity<Usuario> respostaPOST = clienteTeste.withBasicAuth("admin", "password").exchange("/v1/usuario",
				HttpMethod.POST, usuarioEntity_POST, Usuario.class);

		assertEquals(200, respostaPOST.getStatusCodeValue());

		Usuario usuarioPOST = respostaPOST.getBody();
		usuarioPOST.setEmail("mock_email_junit@email.com");
		usuarioPOST.getContatoPorTipo(Tipo.FACEBOOK).setEndereco("mock_junit@facebook.com");

		HttpEntity<Usuario> usuarioEntity_PUT = new HttpEntity<>(usuarioPOST, headers);

		ResponseEntity<Usuario> respostaPUT = clienteTeste.withBasicAuth("admin", "password").exchange("/v1/usuario",
				HttpMethod.PUT, usuarioEntity_PUT, Usuario.class);

		assertNotNull(respostaPUT.getBody());
		assertEquals(200, respostaPUT.getStatusCodeValue());
		assertEquals("mock_junit@facebook.com", respostaPUT.getBody().getContatoPorTipo(Tipo.FACEBOOK).getEndereco());
		assertEquals("mock_email_junit@email.com", respostaPUT.getBody().getEmail());

		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth("admin", "password")
				.exchange("/v1/usuario/" + respostaPUT.getBody().getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}
}
