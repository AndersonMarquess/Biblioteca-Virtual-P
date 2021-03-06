package com.andersonmarques.bvp.config;

import java.util.Arrays;

import com.andersonmarques.bvp.service.JwtTokenService;
import com.andersonmarques.bvp.service.UsuarioAutenticavelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioAutenticavelService usuarioAutenticavelService;
	@Autowired
	private JwtTokenService jwtTokenService;
	
	private final String PUBLIC_ENDPOINTS[] = { "/login", "/v1/usuario" };
	private final String AUTHENTICATED_ENDPOINTS[] = { "/v1/usuario/**", "/v1/livro/**" };
	private final String ADMIN_ENDPOINTS[] = { "/v1/usuario/all" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
				.antMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
				.antMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
			.and()
				.csrf().disable()
				.cors().configurationSource(corsConfigurationSource())
			.and()
				// Usado para impedir a criação de sessão no servidor.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				// Adiciona o filtro antes para permitir a autenticar via token
				.addFilterBefore(new AutenticacaoPorToken(usuarioAutenticavelService, jwtTokenService),
					 UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * Carrega o usuário do banco de dados usando seu username.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioAutenticavelService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	@Bean //Necessário para injetar o ${AuthenticationManager} no controller de login;
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 * Configuração do cors.
	 */
	@Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
   }
}