package com.andersonmarques.bvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.UsuarioRepository;

@Component
public class DBService implements CommandLineRunner{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Tentando persistir informações no banco");
		String senha = new BCryptPasswordEncoder().encode("123");
		usuarioRepository.save(new Usuario("Anderson", senha, "email@email"));
	}
}
