package com.andersonmarques.bvp.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {

	@Value("${bvp.jwt.tempoExpiracao}")
	private Long tempoExpiracao;
	
	@Value("${bvp.jwt.fraseDeSeguranca}")
	private String fraseDeSeguranca;

	public boolean isTokenValido(String token) {
		try {
			// Lança uma exception se o token não for válido.
			Jwts.parser().setSigningKey(fraseDeSeguranca).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getEmailUsuario(String token) {
		return Jwts
				.parser()
				.setSigningKey(fraseDeSeguranca)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public String gerarToken(Authentication authentication) {
		UserDetails userLogado = (UserDetails) authentication.getPrincipal();
		Date dataAtual = new Date();
		Date dataExpiracao = getDataExpircacao();

		return Jwts
				.builder()
				.setIssuer("API Biblioteca Virtual Pública")
				.setSubject(userLogado.getUsername())
				.setIssuedAt(dataAtual)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS512, fraseDeSeguranca)
				.compact();
	}

	private Date getDataExpircacao() {
		return Date.from(
				LocalDateTime
					.now()
					.plusSeconds(tempoExpiracao)
					.atZone(ZoneId.systemDefault())
					.toInstant()
			);
	}
}