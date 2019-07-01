package com.andersonmarques.bvp.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andersonmarques.bvp.service.JwtTokenService;
import com.andersonmarques.bvp.service.UsuarioAutenticavelService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Este filtro não pode ser gerenciado pelo spring e por isso deve ser
 * declarado na classe de configuração de segurança.
 */
public class AutenticacaoPorToken extends OncePerRequestFilter {

	private UsuarioAutenticavelService usuarioAutenticavelService;
	private JwtTokenService jwtTokenService;

	public AutenticacaoPorToken(UsuarioAutenticavelService usuarioAutenticavelService,
			JwtTokenService jwtTokenService) {
		this.usuarioAutenticavelService = usuarioAutenticavelService;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = getToken(request);

		if (jwtTokenService.isTokenValido(token)) {
			autenticarUsuario(token);
		}

		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String prefixo = "Bearer ";

		if (token == null || token.isEmpty() || !token.startsWith(prefixo)) {
			return null;
		}

		// Retorna o conteúdo a partir da palavra Bearer.
		return token.substring(prefixo.length(), token.length());
	}

	private void autenticarUsuario(String token) {
		String email = jwtTokenService.getEmailUsuario(token);
		UserDetails usuario = usuarioAutenticavelService.loadUserByUsername(email);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());

		// Usa o contexto para definir o usuário como autenticado
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}