import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { environment } from '../../../environments/environment';
import { TokenService } from '../token/token.service';

const API_URL = environment.ApiUrl;

// Injetado no SegurancaModule
@Injectable()
export class LoginService {

	constructor(private httpClient: HttpClient, private tokenService: TokenService, private usuariosService: UsuariosService) { }

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
				respostaPOST => {
					const jwtToken = respostaPOST.headers.get("Authorization");
					this.tokenService.setToken(jwtToken);
					this.usuariosService.decodificarENotificar();
				}
			));
	}
}