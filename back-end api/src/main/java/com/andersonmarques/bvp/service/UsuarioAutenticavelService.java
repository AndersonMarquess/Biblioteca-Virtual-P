package com.andersonmarques.bvp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.andersonmarques.bvp.exception.EmailDoUsuarioNaoEncontradoException;
import com.andersonmarques.bvp.model.Permissao;
import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.repository.PermissaoRepository;
import com.andersonmarques.bvp.repository.UsuarioRepository;

/**
 * Classe para fazer gerenciamento de dados para usuários com autenticação.
 * 
 * @author Anderson Marques
 *
 */
@Service
public class UsuarioAutenticavelService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) {
		Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new EmailDoUsuarioNaoEncontradoException(String.format("Usuário com e-mail [ %s ] não encontrado.", email));
		}
		
		return montarUserDetails(usuario.get());
	}

	/**
	 * Monta um {UserDetails} com base no usuário informado e carrega todas as suas
	 * permissões por ID.
	 * 
	 * @param usuario
	 * @return
	 */
	private UserDetails montarUserDetails(Usuario usuario) {
		return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(getPermissoesPorIdUsuario(usuario.getId()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
	}

	/**
	 * Retorna todas as Permissões do usuário com id especificado.
	 * 
	 * @return
	 */
	private Collection<GrantedAuthority> getPermissoesPorIdUsuario(String id) {
		Set<Permissao> permissoes = permissaoRepository.findAllByUsuariosId(id);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		permissoes.forEach(
			p -> authorities.add(
				() -> p.getNomePermissao()
			)
		);
		return authorities;
	}
}