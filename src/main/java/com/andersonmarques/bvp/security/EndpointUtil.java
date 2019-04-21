package com.andersonmarques.bvp.security;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.andersonmarques.bvp.model.Usuario;
import com.andersonmarques.bvp.service.UsuarioService;

/**
 * Classe responsável por disponibilizar verificações e validações.
 * 
 * @author Anderson
 *
 */
public class EndpointUtil {

	/**
	 * Usuários só podem buscar informações, atualizar ou remover a si mesmo, caso
	 * contrario apenas administradores podem realizar essas ações, se nenhuma das
	 * duas condições forem satisfeitas, uma exceção é lançada.
	 * 
	 * @param idAlvo
	 * @param usuarioService
	 * @return boolean
	 */
	public static boolean isUsuarioPermitido(String idAlvo, UsuarioService usuarioService) {
		String emailDoUsuarioLogado = ((UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal()).getUsername();

		Usuario usuarioLogado = usuarioService.buscarUsuarioPorEmail(emailDoUsuarioLogado);

		/* Verifica se o id do usuário logado não é igual ao usuário alvo */
		if (!usuarioLogado.getId().equalsIgnoreCase(idAlvo)) {
			/* Verificar se não tem role administrador */
			if (usuarioLogado.getPermissoes().stream().noneMatch(p -> p.getNomePermissao().equals("ROLE_ADMIN"))) {
				return false;
			}
		}

		return true;
	}

	public static String getJsonParaUnauthorized401(String caminho) {
		String json = "{\"timestamp\":\"" + LocalDateTime.now()
				+ "\",\"status\":401,"
				+ "\"error\":\"Unauthorized\","
				+ "\"message\":\"Unauthorized\","
				+ "\"path\":\"" + caminho + "\"}";
		return json;
	}
}
