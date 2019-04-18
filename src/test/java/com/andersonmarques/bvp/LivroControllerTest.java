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
		
		System.out.println(respostaGET.getBody());
	}
	
	@Test
	public void cadastrarLivroComRoleUserRecebe_StatusCode200() {
		Usuario usuario = new Usuario("Usuario mock 2019", "123", "user_mock@email.com");
		usuario.adicionarContato(new Contato("@userMock_2019", Tipo.TWITTER));
		HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, headers);
		ResponseEntity<Usuario> usuarioPostResposta = clienteTest.withBasicAuth(USER_EMAIL, USER_PASSWORD).exchange("/v1/usuario",
				HttpMethod.POST, usuarioEntity, Usuario.class);
		
		assertEquals(200, usuarioPostResposta.getStatusCodeValue());
		assertNotNull(usuarioPostResposta.getBody());
		
		Livro livro = new Livro("978-85-83b8-bb9-4", "Sandman - Prelúdio 3", "Lindo e confuso",
				"https://img.assinaja.com/assets/tZ/004/img/81104_520x520.jpg", usuarioPostResposta.getBody().getId());
		livro.adicionarCategoria(new Categoria("Fantasia"));
		HttpEntity<Livro> livroEntity = new HttpEntity<>(livro, headers);
		ResponseEntity<Livro> livroPostResposta = clienteTest.withBasicAuth(usuario.getEmail(), usuario.getSenha())
				.exchange("/v1/livro", HttpMethod.POST, livroEntity, Livro.class);
		
		assertEquals(200, livroPostResposta.getStatusCodeValue());
		assertNotNull(livroPostResposta.getBody());
		System.out.println(livroPostResposta.getBody());
	}
}
