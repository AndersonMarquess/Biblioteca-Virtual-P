package com.andersonmarques.bvp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	@Query("SELECT u FROM Usuario u WHERE u.email = ?1")
	Optional<Usuario> findUserByEmail(String email);
}
