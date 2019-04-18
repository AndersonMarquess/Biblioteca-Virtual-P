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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Livro;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LivroControllerTest {

	@Autowired
	private TestRestTemplate clienteTest;

	@Test
	public void listarTodosOsLivrosSemRoleUserRecebe_StatusCode401() {
		ResponseEntity<String> resposta = clienteTest.getForEntity("/v1/livro/all", String.class);
		assertEquals(401, resposta.getStatusCodeValue());
	}

	@Test
	public void listarTodosOsLivrosComRoleUserRecebe_StatusCode200() {
		ResponseEntity<String> resposta = clienteTest.withBasicAuth("necronomicon@email.com", "123")
				.getForEntity("/v1/livro/all", String.class);
		assertEquals(200, resposta.getStatusCodeValue());
	}
	
	private ParameterizedTypeReference<List<Livro>> getTipoListaDeLivros() {
		return new ParameterizedTypeReference<List<Livro>>() {
		};
	}
	
	@Test
	public void mapearLivrosRecuperados() {
		ResponseEntity<List<Livro>> respostaGET = clienteTest.withBasicAuth("necronomicon@email.com", "123")
				.exchange("/v1/livro/all", HttpMethod.GET, null, getTipoListaDeLivros());
		
		assertNotNull(respostaGET.getBody());
		assertEquals(200, respostaGET.getStatusCodeValue());
		
		System.out.println(respostaGET.getBody());
	}
}
