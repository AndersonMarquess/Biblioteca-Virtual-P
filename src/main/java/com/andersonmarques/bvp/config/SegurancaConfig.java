package com.andersonmarques.bvp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	@Autowired
	private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
	
	private final String PUBLIC_ENDPOINTS[] = { "/**" };
	private final String AUTHENTICATED_ENDPOINTS[] = { "/v1/usuario/all" };
	private final String ADMIN_ENDPOINTS[] = { "/v1/usuario/**" };
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
		    	.exceptionHandling()
		    	//Ponto de entrada para autenticação, se o request não for autenticado, 
		    	//será devolvido uma resposta com status code 401 UNAUTHORIZED (O padrão é uma pagina html).
		    	.authenticationEntryPoint(restAuthenticationEntryPoint)
	    	.and()
			    .authorizeRequests()
			    .antMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
			    .antMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
		    .and()
			    .formLogin()
			    //Success Handler personalizado para retornar com status code 200 (padrão é 301)
			    .successHandler(mySuccessHandler)
			    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
		    .and()
		    .logout();
	}

	/**
	 * Usuários Mock para teste com roles diferentes
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
				.and()
				.withUser("user").password(encoder().encode("userPass")).roles("USER");
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}