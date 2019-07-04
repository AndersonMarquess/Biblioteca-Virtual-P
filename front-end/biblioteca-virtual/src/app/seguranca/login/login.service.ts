import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { TokenService } from '../token/token.service';

const API_URL = "http://localhost:8080";

// Injetado no SegurancaModule
@Injectable()
export class LoginService {

	constructor(private httpClient: HttpClient, private tokenService: TokenService) { }

	public autenticar(email: string, senha: string) {
		const endereco = API_URL + "/login";
		const objCredenciais = {
			username: email,
			password: senha
		}

		// Observable só executa o request quando alguém subescrever
		return this.httpClient
			.post(endereco, objCredenciais, { observe: 'response' })
			.pipe(tap(
				resposta => {
					const jwtToken = resposta.headers.get("Authorization");
					this.tokenService.setToken(jwtToken);
				}
			));
	}
}