package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Usuario;

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
	 * Retorna o tipo List<Usuario>.
	 * 
	 * @return List<Usuario>
	 */
	private ParameterizedTypeReference<List<Usuario>> getTipoListaDeUsuario() {
		return new ParameterizedTypeReference<List<Usuario>>() {
		};
	}

	@Test
	public void buscarDetalhesDoUsuarioPorIdComRoleAdminRecebe_StatusCode200() {
		ResponseEntity<List<Usuario>> usuarios = clienteTeste.withBasicAuth("necronomicon", "123")
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());
		
		Usuario usuario = usuarios.getBody().get(0);

		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth("admin", "password")
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);

		assertNotNull(resposta.getBody());
		assertEquals(usuario.getNome(), resposta.getBody().getNome());
		assertEquals(usuario.getEmail(), resposta.getBody().getEmail());
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void buscarDetalhesDoUsuarioPorIdComRoleUserRecebe_StatusCode403() {
		ResponseEntity<List<Usuario>> usuarios = clienteTeste.withBasicAuth("necronomicon", "123")
				.exchange("/v1/usuario/all", HttpMethod.GET, null, getTipoListaDeUsuario());
		
		Usuario usuario = usuarios.getBody().get(0);
		
		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth("necronomicon", "123")
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);

		assertEquals(403, resposta.getStatusCodeValue());
	}
}
