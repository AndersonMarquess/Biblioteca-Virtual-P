package com.andersonmarques.bvp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class LogService {

	private static Logger logger = LoggerFactory.getLogger(LogService.class);
	
	public static void imprimirAcaoDoUsuario(String acao) {
		String nomeUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("O usuário [{}] requisitou ação de [{}]", nomeUsuarioLogado, acao);
	}
}