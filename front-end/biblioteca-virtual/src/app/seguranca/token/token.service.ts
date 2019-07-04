import { Injectable } from '@angular/core';

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
}