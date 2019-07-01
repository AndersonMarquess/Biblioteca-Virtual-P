package com.andersonmarques.bvp.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.andersonmarques.bvp.exception.LoginInvalidoException;
import com.andersonmarques.bvp.model.CredenciaisLogin;
import com.andersonmarques.bvp.service.JwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	// Para ser injetado, precisa ser definido na classe de configuração de
	// segurança.
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenService jwtTokenService;

	@PostMapping("/login")
	public ResponseEntity<CredenciaisLogin> login(@RequestBody @Valid CredenciaisLogin credenciaisLogin,
			HttpServletResponse resp) {
		UsernamePasswordAuthenticationToken dadosLogin = credenciaisLogin.getToken();

		try {
			Authentication authentication = authenticationManager.authenticate(dadosLogin);
			resp.addHeader("Bearer", jwtTokenService.gerarToken(authentication));
			return ResponseEntity.ok().build();
		} catch (Exception exception) {
			// Se os dados de login estiverem errados retorna bad request.
			throw new LoginInvalidoException("E-mail ou senha inválidos.");
		}
	}
}