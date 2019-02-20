package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioTest {

	@Test
	public void criarUsuarioComSucesso() {
		Usuario user = new Usuario();
		user.setNome("Anderson");
		user.setSenha("123");
		user.setEmail("email@contato.com");
	}
	
	@Test
	public void criarUsuarioEPegarInformacoes() {
		Usuario user = new Usuario("Anderson", "123", "email@contato.com");
		assertEquals("Anderson", user.getNome());
		assertEquals("123", user.getSenha());
		assertEquals("email@contato.com", user.getEmail());
	}
	
	@Test
	public void criarDoisUsuariosEPegarInformacoes() {
		Usuario user = new Usuario("Anderson", "123", "email@contato.com");
		assertEquals("Anderson", user.getNome());
		assertEquals("123", user.getSenha());
		assertEquals("email@contato.com", user.getEmail());
		
		Usuario user2 = new Usuario("Pedro", "321", "pedro@contato.com");
		assertEquals("Pedro", user2.getNome());
		assertEquals("321", user2.getSenha());
		assertEquals("pedro@contato.com", user2.getEmail());
	}
	
	
}
