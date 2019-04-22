package com.andersonmarques.bvp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Livro;

@Repository
public interface LivroRepository extends MongoRepository<Livro, String> {

	Livro findFirstLivroByTituloContainsIgnoreCase(String titulo);

	List<Livro> findAllByIdDonoLivro(String id);

}
