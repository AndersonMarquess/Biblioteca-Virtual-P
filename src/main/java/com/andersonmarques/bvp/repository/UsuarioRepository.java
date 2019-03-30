package com.andersonmarques.bvp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	Optional<Usuario> findUsuarioByEmail(String email);
	
}
