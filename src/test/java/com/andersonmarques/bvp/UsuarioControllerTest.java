package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

	private String getStringAleatoria() {
		return UUID.randomUUID().toString();
	}

	private Usuario getUsuarioAleatorio() {
		Usuario usuario = new Usuario(getStringAleatoria(), getStringAleatoria(), getStringAleatoria());
		usuario.adicionarContato(new Contato(getStringAleatoria(), Tipo.TWITTER));
		return usuario;
	}

	private ResponseEntity<List<UsuarioDTO>> listarTodosOsUsuarios(String email, String senha) {
		return clienteTeste.withBasicAuth(email, senha).exchange("/v1/usuario/all", HttpMethod.GET, null,
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
		return clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.getForEntity("/v1/usuario/" + usuario.getId(), Usuario.class);
	}

	private ResponseEntity<String> deletarUsuario(Usuario usuario) {
		return clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
	}

	@Test
	public void autenticacaoComRoleUserParaListarUsuariosRecebe_StatusCode403() {
		ResponseEntity<String> resposta = clienteTeste.withBasicAuth("necronomicon@email.com", "123")
				.getForEntity("/v1/usuario/all", String.class);
		assertEquals(403, resposta.getStatusCodeValue());
	}

	@Test
	public void autenticacaoComRoleAdminParaListarUsuariosRecebe_StatusCode200() {
		ResponseEntity<List<UsuarioDTO>> resposta = listarTodosOsUsuarios(ADMIN_EMAIL, ADMIN_PASSWORD);
		assertEquals(200, resposta.getStatusCodeValue());
	}

	@Test
	public void buscarDetalhesDeSiPorIdRecebe_StatusCode200() {
		/* Criar o usuário */
		Usuario usuario = getUsuarioAleatorio();
		ResponseEntity<Usuario> respostaPOST = postUsuario(usuario);
		assertEquals(200, respostaPOST.getStatusCodeValue());

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
		Usuario usuario = getUsuarioAleatorio();
		Usuario usuario2 = getUsuarioAleatorio();

		ResponseEntity<Usuario> respostaPOST = postUsuario(usuario);
		assertEquals(200, respostaPOST.getStatusCodeValue());
		respostaPOST = postUsuario(usuario2);
		assertEquals(200, respostaPOST.getStatusCodeValue());

		/* Buscar detalhes */
		ResponseEntity<Usuario> resposta = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.getForEntity("/v1/usuario/" + usuario2.getId(), Usuario.class);
		assertNotNull(resposta.getBody());
		assertEquals(401, resposta.getStatusCodeValue());

		/* Remover usuários */
		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());

		ResponseEntity<String> resposta2DELETE = deletarUsuario(usuario2);
		assertEquals(200, resposta2DELETE.getStatusCodeValue());
	}

	@Test
	public void verificarPermissoesDoUsuarioPorId() {
		/* Criar */
		Usuario usuario = getUsuarioAleatorio();

		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertNotNull(usuarioPOST.getBody());
		assertEquals(200, usuarioPOST.getStatusCodeValue());

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
		Usuario usuario = getUsuarioAleatorio();

		ResponseEntity<Usuario> respostaPOST = postUsuario(usuario);
		assertNotNull(respostaPOST.getBody());
		assertEquals(200, respostaPOST.getStatusCodeValue());

		ResponseEntity<String> respostaDELETE = deletarUsuario(usuario);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}

	@Test
	public void usuarioAtualizarSiMesmoRecebe_StatusCode200() {
		/* Criar */
		Usuario usuario = getUsuarioAleatorio();
		System.out.println("INICIO: " + usuario);
		usuario.adicionarContato(new Contato("mock_junit@fb.com", Tipo.FACEBOOK));

		ResponseEntity<Usuario> usuarioPOST = postUsuario(usuario);
		assertNotNull(usuarioPOST.getBody());
		assertEquals(200, usuarioPOST.getStatusCodeValue());

		System.out.println("POS POST: " + usuarioPOST.getBody());

		/* Atualizar */
		Usuario usuarioAtualizado = usuarioPOST.getBody();
		usuarioAtualizado.setNome("Nome atualizado");
		usuarioAtualizado.setEmail("email@atualizado.com");
		usuarioAtualizado.getContatoPorTipo(Tipo.FACEBOOK).setEndereco("atualizado@facebook.com");

		System.out.println("POS EDIT: " + usuarioAtualizado);

		ResponseEntity<Usuario> respostaPUT = clienteTeste.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/usuario", HttpMethod.PUT, new HttpEntity<>(usuarioAtualizado, headers), Usuario.class);
		assertNotNull(respostaPUT.getBody());
		assertEquals(200, respostaPUT.getStatusCodeValue());
		assertEquals("email@atualizado.com", respostaPUT.getBody().getEmail());
		assertEquals("Nome atualizado", respostaPUT.getBody().getNome());

		System.out.println("POS PUT: " + respostaPUT.getBody());
		/* Remover */
		ResponseEntity<String> respostaDELETE = clienteTeste.withBasicAuth("email@atualizado.com", usuario.getSenha())
				.exchange("/v1/usuario/" + usuario.getId(), HttpMethod.DELETE, null, String.class);
		assertEquals(200, respostaDELETE.getStatusCodeValue());
	}
}
