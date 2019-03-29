package com.andersonmarques.bvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

	Livro findFirstLivroByTituloLike(String titulo);

}
