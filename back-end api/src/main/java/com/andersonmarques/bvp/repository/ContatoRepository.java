package com.andersonmarques.bvp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Contato;

@Repository
public interface ContatoRepository extends MongoRepository<Contato, String> {

	Contato findByEndereco(String email);

}
