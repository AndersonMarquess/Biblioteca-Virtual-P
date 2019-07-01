package com.andersonmarques.bvp.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.andersonmarques.bvp.model.ApiErro;
import com.fasterxml.jackson.databind.JsonMappingException;

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

	@ExceptionHandler(value = LoginInvalidoException.class)
	public ResponseEntity<ApiErro> loginInvalidoException(LoginInvalidoException exception,
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

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiErro> erroAoFazerOJsonMapping(JsonMappingException exception,
			HttpServletRequest request) {
		
		ApiErro erro = new ApiErro(HttpStatus.BAD_REQUEST, exception.getOriginalMessage(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErro> validadorDeArgumentos(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		Map<String, String> errors = montarMapComCampoErro(exception);

		ApiErro erro = new ApiErro(HttpStatus.BAD_REQUEST, errors.toString(), request.getRequestURI());
		return ResponseEntity.status(erro.getStatus()).body(erro);
	}

	/**
	 * Retorna um map com todos os erros da bean validation.
	 * 
	 * @author Alejandro Ugarte -
	 *         https://www.baeldung.com/spring-boot-bean-validation
	 * @param exception
	 * @return
	 */
	private Map<String, String> montarMapComCampoErro(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
