package com.andersonmarques.bvp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.andersonmarques.bvp.service.UsuarioAutenticavelService;

@Configuration
@EnableWebSecurity
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	@Autowired
	private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
	@Autowired
	private UsuarioAutenticavelService usuarioAutenticavelService;
	
	private final String PUBLIC_ENDPOINTS[] = { "/v1/usuario" };
	private final String AUTHENTICATED_ENDPOINTS[] = { "/v1/usuario/**", "/v1/livro/**" };
	private final String ADMIN_ENDPOINTS[] = { "/v1/usuario/all" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.httpBasic()
	    	.and()
				// Se nenhum ponto de entrada for definido ao tentar fazer login com as
				// credenciais inválidas, uma página html é retornada.
	    		// Com a definição será retornado 401 Unauthorized.
	    		.exceptionHandling()
	    		.authenticationEntryPoint(restAuthenticationEntryPoint)
	    	.and()
			    .authorizeRequests()
			    .antMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
			    .antMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
			    .antMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
		    .and()
			    .formLogin()
			    //SuccessHandler personalizado para retornar com status code 200 (padrão é 301)
			    .successHandler(mySuccessHandler)
			    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
		    .and()
		    	.csrf().disable();
	}

	/**
	 * Carrega o usuário do banco de dados usando seu username.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioAutenticavelService).passwordEncoder(new BCryptPasswordEncoder());
	}
}