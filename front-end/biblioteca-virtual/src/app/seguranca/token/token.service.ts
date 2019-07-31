import { Injectable } from '@angular/core';
import { UsuarioTokenJWT } from 'src/app/compartilhados/models/usuarioTokenJWT';

import * as jwt_decode from 'jwt-decode';

const CHAVE_TOKEN = "jwtToken";

@Injectable({ providedIn: "root" })
export class TokenService {

	constructor() { }

	possuiToken(): boolean {
		return this.getToken() != null;
	}

	getToken(): string {
		return localStorage.getItem(CHAVE_TOKEN);
	}

	setToken(valorToken: string) {
		const prefixo = "Bearer ";

		console.log(`Salvando token: ${valorToken}`);

		if (valorToken.startsWith(prefixo)) {
			localStorage.setItem(CHAVE_TOKEN, valorToken);
		}
	}

	removerToken(): void {
		localStorage.removeItem(CHAVE_TOKEN);
	}

	tokenEstaExpirado(): boolean {
		if (this.getToken()) {
			const usuarioTokenJWT = jwt_decode(this.getToken()) as UsuarioTokenJWT;
			const dataExpiracao = new Date(usuarioTokenJWT.exp * 1000);
			const dataAtual = new Date();

			return dataAtual > dataExpiracao;
		}
		return true;
	}
}