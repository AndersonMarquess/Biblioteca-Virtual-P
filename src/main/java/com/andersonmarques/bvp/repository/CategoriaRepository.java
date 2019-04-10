package com.andersonmarques.bvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Categoria;

@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, String> {

	Optional<Categoria> findByNome(String nome);

	List<Categoria> findAllByLivrosId(String id);

}
