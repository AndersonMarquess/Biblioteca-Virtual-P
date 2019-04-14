package com.andersonmarques.bvp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.andersonmarques.bvp.model.Permissao;

@Repository
public interface PermissaoRepository extends MongoRepository<Permissao, String> {

	Optional<Permissao> findByNomePermissao(String nomePermissao);

	List<Permissao> findAllByUsuariosId(String id);

}
