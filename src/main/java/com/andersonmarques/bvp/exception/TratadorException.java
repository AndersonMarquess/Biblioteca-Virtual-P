package com.andersonmarques.bvp.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.andersonmarques.bvp.model.ApiErro;

@ControllerAdvice
public class TratadorException {

	@ExceptionHandler(value = UsuarioSemAutorizacaoException.class)
	public ResponseEntity<ApiErro> usuarioSemAutorizacao(UsuarioSemAutorizacaoException exception,
			HttpServletRequest request) {

		ApiErro erro = new ApiErro(HttpStatus.UNAUTHORIZED, exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}

	@ExceptionHandler(value = CategoriaDuplicadaException.class)
	public ResponseEntity<ApiErro> livroComCategoriaDuplicada(CategoriaDuplicadaException exception,
			HttpServletRequest request) {

		ApiErro erro = new ApiErro(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}

	@ExceptionHandler(value = EnderecoDuplicadoException.class)
	public ResponseEntity<ApiErro> enderecoDuplicadoException(EnderecoDuplicadoException exception,
			HttpServletRequest request) {

		ApiErro erro = new ApiErro(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}

	@ExceptionHandler(value = EmailDoUsuarioNaoEncontradoException.class)
	public ResponseEntity<ApiErro> emailDoUsuarioNaoEncontrado(EmailDoUsuarioNaoEncontradoException exception,
			HttpServletRequest request) {

		ApiErro erro = new ApiErro(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}
}
