package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
}
