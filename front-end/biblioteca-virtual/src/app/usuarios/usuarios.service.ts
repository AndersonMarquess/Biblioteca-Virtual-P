import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Usuario } from '../compartilhados/models/usuario';
import { UsuarioTokenJWT } from '../compartilhados/models/usuarioTokenJWT';
import { TokenService } from '../seguranca/token/token.service';

// importa tudo e chama de jwt_decode
import * as jwt_decode from "jwt-decode";

const API_URL = environment.ApiUrl;

@Injectable({ providedIn: "root" })
export class UsuariosService {

	private usuarioSubject = new BehaviorSubject<Usuario>(null);

	constructor(private httpClient: HttpClient, private tokenService: TokenService) {
		if (this.tokenService.possuiToken()) {
			this.decodificarENotificar();
		}
	}

	buscarContatoDoUsuario(idUsuario: string): Observable<any> {
		const endereco = API_URL + "/v1/usuario/contatos/" + idUsuario;
		return this.httpClient.get(endereco);
	}

	buscarInfoUsuarioComEmail(emailUsuario: string): Observable<Usuario> {
		const endereco = API_URL + "/v1/usuario/info/" + emailUsuario;
		return this.httpClient.get<Usuario>(endereco);
	}

	getUsuarioLogado(): Observable<Usuario> {
		return this.usuarioSubject.asObservable();
	}

	decodificarENotificar(): void {
		const token = this.tokenService.getToken();
		const usuarioJWT = jwt_decode(token) as UsuarioTokenJWT;

		this.adicionarUsuarioAoBehavior(usuarioJWT.sub);
	}

	private adicionarUsuarioAoBehavior(email: string) {
		this.buscarInfoUsuarioComEmail(email)
			.subscribe(
				respUser => this.usuarioSubject.next(respUser)
			);
	}

	deslogar(): void {
		this.tokenService.removerToken();
		this.usuarioSubject.next(null);
	}
}